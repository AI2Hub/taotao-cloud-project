package com.taotao.cloud.payment.biz.daxpay.core.channel.config.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.channel.config.convert.PayChannelConfigConvert;
import cn.bootx.platform.daxpay.dto.channel.config.PayChannelConfigDto;
import cn.bootx.platform.daxpay.param.channel.config.PayChannelConfigParam;
import com.baomidou.mybatisplus.annotation.TableName;
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
@DbTable(comment = "支付通道配置")
@Accessors(chain = true)
@TableName("pay_channel_config")
public class PayChannelConfig extends MpBaseEntity implements EntityBaseFunction<PayChannelConfigDto> {

    /** 通道编码 */
    @DbColumn(comment = "通道编码")
    private String code;

    /** 通道名称 */
    @DbColumn(comment = "通道名称")
    private String name;

    /** 图片 */
    @DbColumn(comment = "图片")
    private Long image;

    /** 排序 */
    @DbColumn(comment = "排序")
    private Double sortNo;

    /** 创建对象 */
    public static PayChannelConfig init(PayChannelConfigParam in) {
        return PayChannelConfigConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public PayChannelConfigDto toDto() {
        return PayChannelConfigConvert.CONVERT.convert(this);
    }

}
