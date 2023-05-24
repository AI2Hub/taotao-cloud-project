package com.taotao.cloud.payment.biz.daxpay.exception.payment;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.daxpay.code.PaymentErrorCode;

/**
 * 付款已存在
 *
 * @author xxm
 * @date 2020/12/8
 */
public class PayHasExistedException extends BizException {

    public PayHasExistedException() {
        super(PaymentErrorCode.PAYMENT_HAS_EXISTED, "付款已存在");
    }

}
