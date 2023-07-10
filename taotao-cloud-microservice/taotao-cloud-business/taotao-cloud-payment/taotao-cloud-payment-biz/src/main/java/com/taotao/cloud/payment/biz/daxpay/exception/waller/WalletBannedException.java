package com.taotao.cloud.payment.biz.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 钱包被禁用
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletBannedException extends BizException {

    public WalletBannedException() {
        super(PaymentErrorCode.WALLET_BANNED, "钱包被禁用");
    }

}
