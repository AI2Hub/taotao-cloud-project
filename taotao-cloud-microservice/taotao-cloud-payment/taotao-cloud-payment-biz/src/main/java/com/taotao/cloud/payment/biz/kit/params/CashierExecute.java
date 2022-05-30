package com.taotao.cloud.payment.biz.kit.params;

import com.taotao.cloud.payment.api.enums.CashierEnum;
import com.taotao.cloud.payment.biz.kit.dto.PayParam;
import com.taotao.cloud.payment.biz.kit.dto.PaymentSuccessParams;
import com.taotao.cloud.payment.biz.kit.params.dto.CashierParam;

/**
 * 收银台接口
 */
public interface CashierExecute {

    /**
     * 获取支付参数
     *
     * @param payParam 收银台支付参数
     * @return 收银台所需支付参数
     */
    CashierParam getPaymentParams(PayParam payParam);

    /**
     * 支付成功
     *
     * @param paymentSuccessParams 支付回调
     */
    void paymentSuccess(PaymentSuccessParams paymentSuccessParams);

    /**
     * 支付结果查询
     *
     * @param payParam
     * @return
     */
    Boolean paymentResult(PayParam payParam);

    /**
     * 服务的枚举类型
     *
     * @return
     */
    CashierEnum cashierEnum();
}
