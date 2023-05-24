package com.taotao.cloud.payment.biz.daxpay.param.cashier;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 结算台发起支付参数
 *
 * @author xxm
 * @date 2022/2/23
 */
@Data
@Accessors(chain = true)
@Schema(title = "结算台单支付参数")
public class CashierSinglePayParam {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "openId")
    private String openId;

    @Schema(description = "支付通道")
    private Integer payChannel;

    @Schema(description = "支付方式")
    private Integer payWay;

    @Schema(description = "业务id")
    private String businessId;

    @Schema(description = "付款码")
    private String authCode;

    @Schema(description = "储值卡")
    private String voucherNo;

}
