package com.taotao.cloud.payment.biz.bootx.dto.notify;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xxm
 * @date 2021/6/22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付回调记录")
public class PayNotifyRecordDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = -1241346974397068912L;

    @Schema(description = "支付号")
    private Long paymentId;

    @Schema(description = "通知消息")
    private String notifyInfo;

    @Schema(description = "支付通道")
    private Integer payChannel;

    @Schema(description = "处理状态")
    private Integer status;

    @Schema(description = "提示信息")
    private String msg;

    @Schema(description = "回调时间")
    private LocalDateTime notifyTime;

}
