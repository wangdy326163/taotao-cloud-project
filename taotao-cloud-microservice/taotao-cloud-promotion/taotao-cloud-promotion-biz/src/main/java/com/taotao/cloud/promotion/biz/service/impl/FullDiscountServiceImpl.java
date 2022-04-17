package com.taotao.cloud.promotion.biz.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import com.taotao.cloud.promotion.api.vo.PromotionGoodsSearchParams;
import com.taotao.cloud.promotion.biz.entity.Coupon;
import com.taotao.cloud.promotion.biz.entity.FullDiscount;
import com.taotao.cloud.promotion.biz.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.mapper.FullDiscountMapper;
import com.taotao.cloud.promotion.biz.service.CouponService;
import com.taotao.cloud.promotion.biz.service.FullDiscountService;
import com.taotao.cloud.promotion.biz.service.PromotionGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 满优惠业务层实现
 *
 *
 * @since 2020/8/21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FullDiscountServiceImpl extends AbstractPromotionsServiceImpl<FullDiscountMapper, FullDiscount> implements
	FullDiscountService {

    /**
     * 优惠券
     */
    @Autowired
    private CouponService couponService;
    /**
     * 促销商品
     */
    @Autowired
    private PromotionGoodsService promotionGoodsService;

    @Override
    public List<FullDiscountVO> currentPromotion(List<String> storeId) {
        List<FullDiscountVO> result = new ArrayList<>();
        QueryWrapper<FullDiscount> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(storeId != null && !storeId.isEmpty(), "store_id", storeId);
        queryWrapper.and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START));
        List<FullDiscount> list = this.list(queryWrapper);
        if (list != null) {
            for (FullDiscount fullDiscount : list) {
                PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
                searchParams.setPromotionId(fullDiscount.getId());
                FullDiscountVO fullDiscountVO = new FullDiscountVO(fullDiscount);
                fullDiscountVO.setPromotionGoodsList(promotionGoodsService.listFindAll(searchParams));
                result.add(fullDiscountVO);
            }
        }
        return result;
    }

    /**
     * 获取满优惠活动详情
     *
     * @param id 满优惠KID
     * @return 满优惠活动详情
     */
    @Override
    public FullDiscountVO getFullDiscount(String id) {
        FullDiscount fullDiscount = this.checkFullDiscountExist(id);
        FullDiscountVO fullDiscountVO = new FullDiscountVO(fullDiscount);
        PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
        searchParams.setPromotionId(fullDiscount.getId());
        fullDiscountVO.setPromotionGoodsList(promotionGoodsService.listFindAll(searchParams));
        return fullDiscountVO;
    }

    /**
     * 检查促销参数
     *
     * @param promotions 促销实体
     */
    @Override
    public void checkPromotions(FullDiscount promotions) {
        super.checkPromotions(promotions);
        if (promotions instanceof FullDiscountVO) {
            FullDiscountVO fullDiscountVO = (FullDiscountVO) promotions;
            //验证是否是有效参数
            PromotionTools.paramValid(fullDiscountVO.getStartTime(), fullDiscountVO.getEndTime(), fullDiscountVO.getNumber(), fullDiscountVO.getPromotionGoodsList());
        }

        //当前时间段是否存在同类活动
        this.checkSameActiveExist(promotions.getStartTime(), promotions.getEndTime(), promotions.getStoreId(), promotions.getId());
        //检查满减参数
        this.checkFullDiscount(promotions);

    }

    /**
     * 更新促销商品信息
     *
     * @param promotions 促销实体
     */
    @Override
    public void updatePromotionsGoods(FullDiscount promotions) {
        super.updatePromotionsGoods(promotions);
        if (!PromotionsStatusEnum.CLOSE.name().equals(promotions.getPromotionStatus())
                && PromotionsScopeTypeEnum.PORTION_GOODS.name().equals(promotions.getScopeType())
                && promotions instanceof FullDiscountVO) {
            FullDiscountVO fullDiscountVO = (FullDiscountVO) promotions;
            List<PromotionGoods> promotionGoodsList = PromotionTools.promotionGoodsInit(fullDiscountVO.getPromotionGoodsList(), fullDiscountVO, PromotionTypeEnum.FULL_DISCOUNT);
            this.promotionGoodsService.deletePromotionGoods(Collections.singletonList(promotions.getId()));
            //促销活动商品更新
            this.promotionGoodsService.saveBatch(promotionGoodsList);
        }

    }

    /**
     * 更新促销信息到商品索引
     *
     * @param promotions 促销实体
     */
    @Override
    public void updateEsGoodsIndex(FullDiscount promotions) {
        FullDiscount fullDiscount = JSONUtil.parse(promotions).toBean(FullDiscount.class);
        super.updateEsGoodsIndex(fullDiscount);
    }

    /**
     * 当前促销类型
     *
     * @return 当前促销类型
     */
    @Override
    public PromotionTypeEnum getPromotionType() {
        return PromotionTypeEnum.FULL_DISCOUNT;
    }

    /**
     * 检查满优惠活动是否存在
     *
     * @param id 满优惠活动id
     * @return 满优惠活动
     */
    private FullDiscount checkFullDiscountExist(String id) {
        FullDiscount fullDiscount = this.getById(id);
        if (fullDiscount == null) {
            throw new BusinessException(ResultEnum.FULL_DISCOUNT_NOT_EXIST_ERROR);
        }
        return fullDiscount;
    }

    /**
     * 检查满减参数
     *
     * @param fullDiscount 满减参数信息
     */
    private void checkFullDiscount(FullDiscount fullDiscount) {
        if (fullDiscount.getIsFullMinus() == null && fullDiscount.getIsCoupon() == null && fullDiscount.getIsGift() == null
                && fullDiscount.getIsPoint() == null && fullDiscount.getIsFullRate() == null) {
            throw new BusinessException(ResultEnum.FULL_DISCOUNT_WAY_ERROR);
        }
        //如果优惠方式是满减
        if (Boolean.TRUE.equals(fullDiscount.getIsFullMinus())) {
            this.checkFullMinus(fullDiscount.getFullMinus(), fullDiscount.getFullMoney());
            fullDiscount.setTitle("满" + fullDiscount.getFullMoney() + " 减" + fullDiscount.getFullMinus());
        }
        //如果优惠方式是赠品
        if (Boolean.TRUE.equals(fullDiscount.getIsGift())) {
            //是否没有选择赠品
            boolean noGiftSelected = fullDiscount.getGiftId() == null;
            if (noGiftSelected) {
                throw new BusinessException(ResultEnum.FULL_DISCOUNT_GIFT_ERROR);
            }
        } else {
            fullDiscount.setGiftId(null);
        }
        //如果优惠方式是赠优惠券
        if (Boolean.TRUE.equals(fullDiscount.getIsCoupon())) {
            this.checkCoupon(fullDiscount.getCouponId());
        } else {
            fullDiscount.setCouponId(null);
        }
        //如果优惠方式是折扣
        if (Boolean.TRUE.equals(fullDiscount.getIsFullRate())) {
            this.checkFullRate(fullDiscount.getFullRate());
            fullDiscount.setTitle("满" + fullDiscount.getFullMoney() + " 打" + fullDiscount.getFullRate() + "折");
        }

    }

    /**
     * 检查同一时间段内不能存在相同的活动数量
     *
     * @param statTime 开始时间
     * @param endTime  结束时间
     * @param storeId  店铺id
     * @param id       满优惠活动ID
     */
    private void checkSameActiveExist(Date statTime, Date endTime, String storeId, String id) {
        //同一时间段内相同的活动
        QueryWrapper<FullDiscount> queryWrapper = PromotionTools.checkActiveTime(statTime, endTime, PromotionTypeEnum.FULL_DISCOUNT, storeId, id);
        long sameNum = this.count(queryWrapper);
        if (sameNum > 0) {
            throw new BusinessException(ResultEnum.PROMOTION_SAME_ACTIVE_EXIST);
        }
    }

    /**
     * 检查优惠券信息
     *
     * @param couponId 优惠券编号
     */
    private void checkCoupon(String couponId) {
        //是否没有选择优惠券
        boolean noCouponSelected = couponId == null;
        if (noCouponSelected) {
            throw new BusinessException(ResultEnum.COUPON_NOT_EXIST);
        }
        Coupon coupon = this.couponService.getById(couponId);
        if (coupon == null) {
            throw new BusinessException(ResultEnum.COUPON_NOT_EXIST);
        }
    }

    /**
     * 检查满减信息
     *
     * @param fullMinus 满减金额
     * @param fullMoney 优惠门槛
     */
    private void checkFullMinus(BigDecimal fullMinus, BigDecimal fullMoney) {
        //是否没有填写满减金额
        boolean noFullMinusInput = fullMinus == null || fullMinus == 0;
        if (noFullMinusInput) {
            throw new BusinessException(ResultEnum.FULL_DISCOUNT_MONEY_ERROR);
        }
        if (fullMinus > fullMoney) {
            throw new BusinessException(ResultEnum.FULL_DISCOUNT_MONEY_GREATER_THAN_MINUS);
        }
    }

    /**
     * 检查打折信息
     *
     * @param fullRate 打折数值
     */
    private void checkFullRate(BigDecimal fullRate) {
        //是否没有填写打折数值
        boolean noFullRateInput = fullRate == null || fullRate == 0;
        if (noFullRateInput) {
            throw new BusinessException(ResultEnum.FULL_RATE_NUM_ERROR);
        }
        int rateLimit = 10;
        if (fullRate >= rateLimit || fullRate <= 0) {
            throw new BusinessException(ResultEnum.FULL_RATE_NUM_ERROR);
        }
    }
}
