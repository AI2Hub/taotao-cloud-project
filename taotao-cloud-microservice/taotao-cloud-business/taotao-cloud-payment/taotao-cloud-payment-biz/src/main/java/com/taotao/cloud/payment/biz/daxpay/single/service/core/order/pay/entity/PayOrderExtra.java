package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity;


import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.convert.PayOrderConvert;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.pay.PayOrderExtraDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 支付订单扩展信息
 * @author xxm
 * @since 2023/12/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "支付订单扩展信息")
@TableName("pay_order_extra")
public class PayOrderExtra extends MpBaseEntity implements EntityBaseFunction<PayOrderExtraDto> {

    /** 描述 */
    @DbColumn(comment = "描述")
    private String description;

    /** 同步跳转地址, 以最后一次为准 */
    @DbColumn(comment = "同步跳转地址")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String returnUrl;

    /** 回调通知时是否需要进行签名, 以最后一次为准 */
    @DbColumn(comment = "回调通知时是否需要进行签名")
    private boolean noticeSign;

    /** 异步通知地址 以最后一次为准 */
    @DbColumn(comment = "异步通知地址，以最后一次为准")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String notifyUrl;

    /** 商户扩展参数,回调时会原样返回 以最后一次为准 */
    @DbColumn(comment = "商户扩展参数")
    private String attach;

    /** 请求签名类型 */
    @DbColumn(comment = "签名类型")
    private String reqSignType;

    /** 请求签名值，以最后一次为准 */
    @DbColumn(comment = "签名")
    private String reqSign;

    /** 请求时间，时间戳转时间, 以最后一次为准 */
    @DbColumn(comment = "请求时间，传输时间戳，以最后一次为准")
    private LocalDateTime reqTime;

    /** 支付终端ip 以最后一次为准 */
    @DbColumn(comment = "支付终端ip")
    private String clientIp;

    /** 请求链路ID 以最后一次为准 */
    @DbColumn(comment = "请求链路ID")
    private String reqId;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    private String errorMsg;

    /**
     * 转换
     */
    @Override
    public PayOrderExtraDto toDto() {
        return PayOrderConvert.CONVERT.convert(this);
    }
}
