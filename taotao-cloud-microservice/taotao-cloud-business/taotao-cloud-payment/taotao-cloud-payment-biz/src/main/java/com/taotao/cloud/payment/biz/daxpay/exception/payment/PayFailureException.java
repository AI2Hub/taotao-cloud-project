package com.taotao.cloud.payment.biz.daxpay.exception.payment;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 付款错误
 *
 * @author xxm
 * @since 2020/12/8
 */
public class PayFailureException extends BizException {

    public PayFailureException(String message) {
        super(PaymentErrorCode.PAY_FAILURE, message);
    }

    public PayFailureException() {
        super(PaymentErrorCode.PAY_FAILURE, "支付失败");
    }

}
