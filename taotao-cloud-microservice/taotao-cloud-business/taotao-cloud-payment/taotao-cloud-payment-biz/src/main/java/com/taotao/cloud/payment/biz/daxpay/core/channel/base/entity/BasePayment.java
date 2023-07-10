package com.taotao.cloud.payment.biz.daxpay.core.channel.base.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 基础支付记录类
 *
 * @author xxm
 * @since 2021/2/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class BasePayment extends MpBaseEntity {

    /** 交易记录ID */
    @DbMySqlIndex(comment = "交易记录ID")
    private Long paymentId;

    /** 交易金额 */
    @DbColumn(comment = "交易金额")
    private BigDecimal amount;

    /** 可退款金额 */
    @DbColumn(comment = "可退款金额")
    private BigDecimal refundableBalance;

    /** 关联的业务id */
    @DbMySqlIndex(comment = "业务id索引")
    @DbColumn(comment = "关联的业务id")
    private String businessId;

    /**
     * 支付状态
     * @see PayStatusCode#TRADE_PROGRESS
     */
    @DbColumn(comment = "支付状态")
    private String payStatus;

    /** 支付时间 */
    @DbColumn(comment = "支付时间")
    private LocalDateTime payTime;

}
