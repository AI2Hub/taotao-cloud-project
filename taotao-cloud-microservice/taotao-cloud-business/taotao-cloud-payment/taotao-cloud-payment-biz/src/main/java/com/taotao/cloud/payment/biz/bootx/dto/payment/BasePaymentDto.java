package com.taotao.cloud.payment.biz.bootx.dto.payment;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.daxpay.code.pay.PayStatusCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xxm
 * @date 2021/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "具体支付日志基类")
public class BasePaymentDto extends BaseDto {

    @Schema(description = "支付id")
    private Long paymentId;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "关联的业务id")
    private String businessId;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "可退款金额")
    private BigDecimal refundableBalance;

    /**
     * @see PayStatusCode
     */
    @Schema(description = "支付状态")
    private int payStatus;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

}
