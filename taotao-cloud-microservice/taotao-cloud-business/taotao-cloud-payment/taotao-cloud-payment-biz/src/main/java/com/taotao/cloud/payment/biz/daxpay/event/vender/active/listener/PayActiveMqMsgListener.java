package com.taotao.cloud.payment.biz.daxpay.event.vender.active.listener;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * activeMQ 消息接收
 * @author xxm
 * @since 2023/7/20
 */
@Component
@ConditionalOnProperty(name ="bootx.daxpay.mq-type", havingValue = "active")
public class PayActiveMqMsgListener {
}
