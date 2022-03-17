package com.taotao.cloud.order.biz.roketmq.event.impl;

import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.dto.order.OrderMessage;
import com.taotao.cloud.order.biz.roketmq.event.OrderStatusChangeEvent;
import com.taotao.cloud.order.biz.roketmq.event.TradeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信消息执行器
 */
@Service
public class WechatMessageExecute implements OrderStatusChangeEvent, TradeEvent {

	@Autowired
	private WechatMessageUtil wechatMessageUtil;

	@Override
	public void orderCreate(TradeDTO tradeDTO) {
		for (OrderVO orderVO : tradeDTO.getOrderVO()) {
			try {
				wechatMessageUtil.sendWechatMessage(orderVO.getSn());
			} catch (Exception e) {
				log.error("微信消息发送失败：" + orderVO.getSn(), e);
			}
		}
	}

	@Override
	public void orderChange(OrderMessage orderMessage) {

		switch (orderMessage.getNewStatus()) {
			case PAID:
			case UNDELIVERED:
			case DELIVERED:
			case COMPLETED:
				try {
					wechatMessageUtil.sendWechatMessage(orderMessage.getOrderSn());
				} catch (Exception e) {
					log.error("微信消息发送失败", e);
				}
				break;
			default:
				break;
		}

	}
}
