package com.taotao.cloud.order.api.vo.cart;

import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * 订单导出VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Schema(description = "订单导出DTO")
public record OrderExportVO(
	@Schema(description = "订单编号")
	String sn,

	@Schema(description = "创建时间")
	LocalDateTime createTime,

	@Schema(description = "用户名")
	String memberName,

	@Schema(description = "收件人姓名")
	String consigneeName,

	@Schema(description = "收件人手机")
	String consigneeMobile,

	@Schema(description = "收件人地址")
	String consigneeAddressPath,

	@Schema(description = "详细地址")
	String consigneeDetail,

	@Schema(description = "支付方式")
	String paymentMethod,

	@Schema(description = "物流公司名称")
	String logisticsName,

	@Schema(description = "运费")
	BigDecimal freightPrice,

	@Schema(description = "商品价格")
	BigDecimal goodsPrice,

	@Schema(description = "优惠的金额")
	BigDecimal discountPrice,

	@Schema(description = "总价格")
	BigDecimal flowPrice,

	@Schema(description = "商品名称")
	String goodsName,

	@Schema(description = "商品数量")
	Integer num,

	@Schema(description = "买家订单备注")
	String remark,

	/**
	 * @see OrderStatusEnum
	 */
	@Schema(description = "订单状态")
	String orderStatus,

	/**
	 * @see PayStatusEnum
	 */
	@Schema(description = "付款状态")
	String payStatus,

	/**
	 * @see DeliverStatusEnum
	 */
	@Schema(description = "货运状态")
	String deliverStatus,

	@Schema(description = "是否需要发票")
	Boolean needReceipt,

	@Schema(description = "店铺名称")
	String storeName
) implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;


}
