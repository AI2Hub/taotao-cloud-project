package com.taotao.cloud.payment.biz.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.FatalException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 钱包日志错误
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletLogError extends FatalException {

    public WalletLogError() {
        super(PaymentErrorCode.WALLET_LOG_ERROR, "钱包日志错误");
    }

}
