package com.taotao.cloud.order.api.vo.aftersale;

import com.taotao.cloud.order.api.enums.trade.AfterSaleRefundWayEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 售后申请VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "售后申请VO")
public class AfterSaleApplyVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "申请退款金额单价")
	private BigDecimal applyRefundPrice;

	@Schema(description = "可申请数量")
	private Integer num;

	@Schema(description = "订单子项编号")
	private String orderItemSn;

	@Schema(description = "商品ID")
	private Long goodsId;

	@Schema(description = "货品ID")
	private Long skuId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品图片")
	private String image;

	@Schema(description = "商品价格")
	private BigDecimal goodsPrice;

	/**
	 * @see AfterSaleRefundWayEnum
	 */
	@Schema(description = "退款方式", allowableValues = "ORIGINAL,OFFLINE")
	private String refundWay;

	@Schema(description = "账号类型", allowableValues = "ALIPAY,WECHATPAY,MEMBERWALLET,BANKTRANSFER")
	private String accountType;

	@Schema(description = "是否支持退货")
	private Boolean returnGoods;

	@Schema(description = "是否支持退款")
	private Boolean returnMoney;

	@Schema(description = "会员ID")
	private Long memberId;


}
