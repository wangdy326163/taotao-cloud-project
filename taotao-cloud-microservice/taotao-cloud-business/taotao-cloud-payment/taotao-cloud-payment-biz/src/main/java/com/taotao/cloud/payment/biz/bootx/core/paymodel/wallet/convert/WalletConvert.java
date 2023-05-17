package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.convert;

import cn.bootx.daxpay.core.paymodel.wallet.entity.Wallet;
import cn.bootx.daxpay.core.paymodel.wallet.entity.WalletLog;
import cn.bootx.daxpay.core.paymodel.wallet.entity.WalletPayment;
import cn.bootx.daxpay.dto.paymodel.wallet.WalletDto;
import cn.bootx.daxpay.dto.paymodel.wallet.WalletLogDto;
import cn.bootx.daxpay.dto.paymodel.wallet.WalletPaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 转换
 *
 * @author xxm
 * @date 2021/8/20
 */
@Mapper
public interface WalletConvert {

    WalletConvert CONVERT = Mappers.getMapper(WalletConvert.class);

    WalletDto convert(Wallet in);

    WalletPaymentDto convert(WalletPayment in);

    WalletLogDto convert(WalletLog walletLog);

}
