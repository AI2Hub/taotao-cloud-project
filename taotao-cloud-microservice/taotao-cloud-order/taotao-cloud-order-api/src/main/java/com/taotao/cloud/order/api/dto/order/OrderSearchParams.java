package com.taotao.cloud.order.api.dto.order;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.common.utils.date.DateUtil;
import com.taotao.cloud.order.api.enums.order.CommentStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderPromotionTypeEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderTagEnum;
import com.taotao.cloud.order.api.enums.order.OrderTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单查询参数
 */
@Data
@Schema(description = "订单查询参数")
public class OrderSearchParams extends PageVO {

	private static final long serialVersionUID = -6380573339089959194L;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "订单编号")
	private String orderSn;

	@Schema(description = "页面标签",
		example = "ALL:全部," +
			"WAIT_PAY:待付款," +
			"WAIT_ROG:待收货," +
			"CANCELLED:已取消," +
			"COMPLETE:已完成")
	private String tag;

	@Schema(description = "商家ID")
	private String storeId;

	@Schema(description = "会员ID")
	private String memberId;

	@Schema(description = "收货人")
	private String shipName;

	@Schema(description = "买家昵称")
	private String buyerName;

	@Schema(description = "订单状态")
	private String orderStatus;

	@Schema(description = "付款状态")
	private String payStatus;

	@Schema(description = "关键字 商品名称/买家名称/店铺名称")
	private String keywords;

	@Schema(description = "付款方式")
	private String paymentType;

	/**
	 * @see OrderTypeEnum
	 * @see OrderPromotionTypeEnum
	 */
	@Schema(description = "订单类型", allowableValues = "NORMAL,VIRTUAL,GIFT,PINTUAN,POINT")
	private String orderType;

	@Schema(description = "支付方式")
	private String paymentMethod;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "支付时间")
	private Date paymentTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "下单开始时间")
	private Date startDate;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "下单结束时间")
	private Date endDate;

	@Schema(description = "订单来源")
	private String clientType;

	/**
	 * @see CommentStatusEnum
	 */
	@Schema(description = "评论状态:未评论(UNFINISHED),待追评(WAIT_CHASE),评论完成(FINISHED)，")
	private String commentStatus;

	@Schema(description = "是否为其他订单下的订单，如果是则为依赖订单的sn，否则为空")
	private String parentOrderSn;

	@Schema(description = "是否为某订单类型的订单，如果是则为订单类型的id，否则为空")
	private String promotionId;

	/**
	 * @see OrderPromotionTypeEnum
	 */
	@Schema(description = "订单促销类型")
	private String orderPromotionType;

	public <T> QueryWrapper<T> queryWrapper() {
		AuthUser currentUser = UserContext.getCurrentUser();
		QueryWrapper<T> wrapper = new QueryWrapper<>();

		//关键字查询
		if (CharSequenceUtil.isNotEmpty(keywords)) {
			wrapper.like("o.sn", keywords).or().like("oi.goods_name", keywords);
		}
		if (currentUser != null) {
			//按卖家查询
			wrapper.eq(
				CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.STORE.name()),
				"o.store_id", currentUser.getStoreId());

			//店铺查询
			wrapper.eq(
				CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MANAGER.name())
					&& CharSequenceUtil.isNotEmpty(storeId), "o.store_id", storeId);

			//按买家查询
			wrapper.eq(
				CharSequenceUtil.equals(currentUser.getRole().name(), UserEnums.MEMBER.name())
					&& memberId == null, "o.member_id", currentUser.getId());

		}
		//按照买家查询
		wrapper.like(CharSequenceUtil.isNotEmpty(memberId), "o.member_id", memberId);

		//按订单编号查询
		wrapper.like(CharSequenceUtil.isNotEmpty(orderSn), "o.sn", orderSn);

		//按时间查询
		wrapper.ge(startDate != null, "o.create_time", startDate);

		wrapper.le(endDate != null, "o.create_time", DateUtil.endOfDate(endDate));
		//按购买人用户名
		wrapper.like(CharSequenceUtil.isNotEmpty(buyerName), "o.member_name", buyerName);

		//按订单类型
		wrapper.eq(CharSequenceUtil.isNotEmpty(orderType), "o.order_type", orderType);

		//物流查询
		wrapper.like(CharSequenceUtil.isNotEmpty(shipName), "o.consignee_name", shipName);

		//按商品名称查询
		wrapper.like(CharSequenceUtil.isNotEmpty(goodsName), "oi.goods_name", goodsName);

		//付款方式
		wrapper.like(CharSequenceUtil.isNotEmpty(paymentType), "o.payment_type", paymentType);

		//按支付方式
		wrapper.eq(CharSequenceUtil.isNotEmpty(paymentMethod), "o.payment_method", paymentMethod);

		//订单状态
		wrapper.eq(CharSequenceUtil.isNotEmpty(orderStatus), "o.order_status", orderStatus);

		//付款状态
		wrapper.eq(CharSequenceUtil.isNotEmpty(payStatus), "o.pay_status", payStatus);

		//订单来源
		wrapper.like(CharSequenceUtil.isNotEmpty(clientType), "o.client_type", clientType);

		//按评价状态
		wrapper.eq(CharSequenceUtil.isNotEmpty(commentStatus), "oi.comment_status", commentStatus);

		//按标签查询
		if (CharSequenceUtil.isNotEmpty(tag)) {
			String orderStatusColumn = "o.order_status";
			OrderTagEnum tagEnum = OrderTagEnum.valueOf(tag);
			switch (tagEnum) {
				//待付款
				case WAIT_PAY:
					wrapper.eq(orderStatusColumn, OrderStatusEnum.UNPAID.name());
					break;
				//待发货
				case WAIT_SHIP:
					wrapper.eq(orderStatusColumn, OrderStatusEnum.UNDELIVERED.name());
					break;
				//待收货
				case WAIT_ROG:
					wrapper.eq(orderStatusColumn, OrderStatusEnum.DELIVERED.name());
					break;
				//已取消
				case CANCELLED:
					wrapper.eq(orderStatusColumn, OrderStatusEnum.CANCELLED.name());
					break;
				//已完成
				case COMPLETE:
					wrapper.eq(orderStatusColumn, OrderStatusEnum.COMPLETED.name());
					break;
				default:
					break;
			}
		}

		// 依赖订单
		wrapper.eq(parentOrderSn != null, "o.parent_order_sn", parentOrderSn);
		// 促销活动id
		wrapper.eq(CharSequenceUtil.isNotEmpty(promotionId), "o.promotion_id", promotionId);

		wrapper.eq(CharSequenceUtil.isNotEmpty(orderPromotionType), "o.order_promotion_type",
			orderPromotionType);

		wrapper.eq("o.delete_flag", false);
		return wrapper;
	}

}
