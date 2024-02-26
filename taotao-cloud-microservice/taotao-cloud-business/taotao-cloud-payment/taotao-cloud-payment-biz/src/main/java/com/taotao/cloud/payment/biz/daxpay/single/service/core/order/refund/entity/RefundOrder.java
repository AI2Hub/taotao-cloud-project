package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.convert.RefundOrderConvert;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.refund.RefundOrderDto;
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
 * 退款记录
 * 主键作为退款的请求号
 *
 * @author xxm
 * @since 2022/3/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@DbTable(comment = "退款订单")
@TableName(value = "pay_refund_order", autoResultMap = true)
public class RefundOrder extends MpBaseEntity implements EntityBaseFunction<RefundOrderDto> {

    /** 原支付id */
    @DbColumn(comment = "原支付id")
    private Long paymentId;

    /** 原支付业务号 */
    @DbColumn(comment = "原支付业务号")
    private String businessNo;

    /** 原支付标题 */
    @DbColumn(comment = "原支付标题")
    private String title;

    /**
     * 需要保证全局唯一
     */
    @DbColumn(comment = "退款号")
    private String refundNo;

    /** 退款时是否是含有异步通道 */
    @DbColumn(comment = "是否含有异步通道")
    private boolean asyncPay;

    /**
     * 异步通道
     * @see PayChannelEnum#ASYNC_TYPE_CODE
     */
    @DbColumn(comment = "异步通道")
    private String asyncChannel;

    /** 如果有异步通道, 保存关联的网关订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;


    /** 订单金额 */
    @DbColumn(comment = "订单金额")
    private Integer orderAmount;

    /** 退款金额 */
    @DbColumn(comment = "退款金额")
    private Integer amount;

    /** 剩余可退 */
    @DbColumn(comment = "剩余可退")
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private Integer refundableBalance;

    /** 退款原因 */
    @DbColumn(comment = "退款原因")
    private String reason;

    /** 退款时间 */
    @DbColumn(comment = "退款完成时间")
    private LocalDateTime refundTime;

    /**
     * 退款状态
     * @see RefundStatusEnum
     */
    @DbColumn(comment = "退款状态")
    private String status;

    /** 错误码 */
    @DbColumn(comment = "错误码")
    private String errorCode;

    /** 错误信息 */
    @DbColumn(comment = "错误信息")
    private String errorMsg;

    @Override
    public RefundOrderDto toDto() {
        return RefundOrderConvert.CONVERT.convert(this);
    }

}
