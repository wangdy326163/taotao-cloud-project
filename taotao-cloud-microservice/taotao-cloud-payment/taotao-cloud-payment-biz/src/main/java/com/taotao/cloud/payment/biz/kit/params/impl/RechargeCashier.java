package com.taotao.cloud.payment.biz.kit.params.impl;

import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.CashierExecute;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 充值信息获取
 *
 */

@Component
public class RechargeCashier implements CashierExecute {
    /**
     * 余额
     */
    @Autowired
    private RechargeService rechargeService;
    /**
     * 设置
     */
    @Autowired
    private SettingService settingService;


    @Override
    public CashierEnum cashierEnum() {
        return CashierEnum.RECHARGE;
    }

    @Override
    public void paymentSuccess(PaymentSuccessParams paymentSuccessParams) {
        PayParam payParam = paymentSuccessParams.getPayParam();
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            rechargeService.paySuccess(payParam.getSn(), paymentSuccessParams.getReceivableNo(),paymentSuccessParams.getPaymentMethod());
            log.info("会员充值-订单号{},第三方流水：{}", payParam.getSn(), paymentSuccessParams.getReceivableNo());
        }
    }


    @Override
    public CashierParam getPaymentParams(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            //准备返回的数据
            CashierParam cashierParam = new CashierParam();
            //订单信息获取
            Recharge recharge = rechargeService.getRecharge(payParam.getSn());

            //如果订单已支付，则不能发器支付
            if (recharge.getPayStatus().equals(PayStatusEnum.PAID.name())) {
                throw new BusinessException(ResultEnum.PAY_BigDecimal_ERROR);
            }


            cashierParam.setPrice(recharge.getRechargeMoney());

            try {
                BaseSetting baseSetting = JSONUtil.toBean(settingService.get(SettingEnum.BASE_SETTING.name()).getSettingValue(), BaseSetting.class);
                cashierParam.setTitle(baseSetting.getSiteName());
            } catch (Exception e) {
                cashierParam.setTitle("多用户商城，在线充值");
            }
            cashierParam.setDetail("余额充值");
            cashierParam.setCreateTime(recharge.getCreateTime());
            return cashierParam;
        }

        return null;
    }

    @Override
    public Boolean paymentResult(PayParam payParam) {
        if (payParam.getOrderType().equals(CashierEnum.RECHARGE.name())) {
            Recharge recharge = rechargeService.getRecharge(payParam.getSn());
            if (recharge != null) {
                return recharge.getPayStatus().equals(PayStatusEnum.PAID.name());
            } else {
                throw new BusinessException(ResultEnum.PAY_NOT_EXIST_ORDER);
            }
        }
        return false;
    }
}
