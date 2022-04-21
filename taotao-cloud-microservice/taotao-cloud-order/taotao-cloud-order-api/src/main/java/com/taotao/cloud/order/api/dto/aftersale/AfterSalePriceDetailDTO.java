package com.taotao.cloud.order.api.dto.aftersale;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商城退款流水
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "商城退款流水")
public class AfterSalePriceDetailDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = 8808470688518188146L;

	@Schema(description = "商品总金额（商品原价）")
	private BigDecimal goodsPrice;

	@Schema(description = "配送费")
	private BigDecimal freightPrice;

	//============discount price============

	@Schema(description = "支付积分")
	private Integer payPoint;

	@Schema(description = "优惠金额")
	private BigDecimal discountPrice;

	@Schema(description = "优惠券金额")
	private BigDecimal couponPrice;

	//===========end discount price =============

	//=========distribution==========

	@Schema(description = "单品分销返现支出")
	private BigDecimal distributionCommission;


	@Schema(description = "平台收取交易佣金")
	private BigDecimal platFormCommission;

	//=========end distribution==========

	//========= platform coupon==========

	@Schema(description = "平台优惠券 使用金额")
	private BigDecimal siteCouponPrice;

	@Schema(description = "站点优惠券佣金比例")
	private BigDecimal siteCouponPoint;

	@Schema(description = "站点优惠券佣金")
	private BigDecimal siteCouponCommission;
	//=========end platform coupon==========

	@Schema(description = "流水金额(入账 出帐金额) = goodsPrice - discountPrice - couponPrice")
	private BigDecimal flowPrice;

	@Schema(description = "最终结算金额 = flowPrice - platFormCommission - distributionCommission")
	private BigDecimal billPrice;

	/**
	 * 参与的促销活动
	 */
	@Schema(description = "参与的促销活动")
	//private List<BasePromotions> joinPromotion;
	private List<String> joinPromotion;

}
