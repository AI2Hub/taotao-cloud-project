package com.taotao.cloud.payment.biz.bootx.mq;

import cn.bootx.daxpay.code.PaymentEventCode;
import cn.bootx.daxpay.event.PayCancelEvent;
import cn.bootx.daxpay.event.PayCompleteEvent;
import cn.bootx.daxpay.event.PayRefundEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 支付中心消息发送器
 *
 * @author xxm
 * @date 2021/4/22
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventSender {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 支付完成 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayComplete(PayCompleteEvent event) {
        rabbitTemplate.convertAndSend(PaymentEventCode.EXCHANGE_PAYMENT, PaymentEventCode.PAY_COMPLETE, event);
    }

    /**
     * 支付撤销/关闭 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayCancel(PayCancelEvent event) {
        rabbitTemplate.convertAndSend(PaymentEventCode.EXCHANGE_PAYMENT, PaymentEventCode.PAY_CANCEL, event);
    }

    /**
     * 支付退款 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPayRefund(PayRefundEvent event) {
        rabbitTemplate.convertAndSend(PaymentEventCode.EXCHANGE_PAYMENT, PaymentEventCode.PAY_REFUND, event);
    }

    /**
     * 支付单超时 事件发布
     */
    @Async("bigExecutor")
    @Retryable(value = Exception.class)
    public void sendPaymentExpiredTime(Long paymentId) {
        rabbitTemplate.convertAndSend(PaymentEventCode.EXCHANGE_PAYMENT, PaymentEventCode.PAYMENT_EXPIRED_TIME,
                paymentId);
    }

}
