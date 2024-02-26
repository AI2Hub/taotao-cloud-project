package com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.close;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.table.modify.annotation.DbColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付关闭记录
 * @author xxm
 * @since 2024/1/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付关闭记录")
public class PayCloseRecordDto extends BaseDto {

    /** 支付记录id */
    @Schema(description = "支付记录id")
    private Long paymentId;

    /** 业务号 */
    @Schema(description = "业务号")
    private String businessNo;

    /**
     * 关闭的异步支付通道, 可以为空
     * @see PayChannelEnum#getCode()
     */
    @Schema(description = "关闭的异步支付通道")
    private String asyncChannel;

    /**
     * 是否关闭成功
     */
    @Schema(description = "是否关闭成功")
    private boolean closed;

    /** 错误消息 */
    @Schema(description = "错误消息")
    private String errorMsg;

    /** 客户端IP */
    @DbColumn(comment = "客户端IP")
    private String clientIp;

    /** 请求链路ID */
    @DbColumn(comment = "请求链路ID")
    private String reqId;
}
