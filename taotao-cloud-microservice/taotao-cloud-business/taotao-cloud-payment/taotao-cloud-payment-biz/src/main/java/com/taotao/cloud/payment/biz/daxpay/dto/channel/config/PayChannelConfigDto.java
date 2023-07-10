package com.taotao.cloud.payment.biz.daxpay.dto.channel.config;

import cn.bootx.platform.common.core.rest.dto.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付通道配置
 *
 * @author xxm
 * @since 2023-05-24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(title = "支付通道配置")
@Accessors(chain = true)
public class PayChannelConfigDto extends BaseDto {

    @Schema(description = "通道编码")
    private String code;

    @Schema(description = "通道名称")
    private String name;

    @Schema(description = "图片")
    private Long image;

    @Schema(description = "排序")
    private Double sortNo;

}
