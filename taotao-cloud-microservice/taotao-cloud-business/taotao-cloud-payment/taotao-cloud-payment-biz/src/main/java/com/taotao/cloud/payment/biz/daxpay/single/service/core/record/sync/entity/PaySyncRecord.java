package com.taotao.cloud.payment.biz.daxpay.single.service.core.record.sync.entity;

import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpCreateEntity;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.RefundSyncStatusEnum;
import cn.bootx.platform.daxpay.code.PaySyncStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.code.PaymentTypeEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.record.sync.convert.PaySyncRecordConvert;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.sync.PaySyncRecordDto;
import cn.bootx.table.modify.annotation.DbColumn;
import cn.bootx.table.modify.annotation.DbTable;
import cn.bootx.table.modify.mysql.annotation.DbMySqlFieldType;
import cn.bootx.table.modify.mysql.constants.MySqlFieldTypeEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付同步订单
 * @author xxm
 * @since 2023/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@DbTable(comment = "支付同步订单")
@Accessors(chain = true)
@TableName("pay_sync_record")
public class PaySyncRecord extends MpCreateEntity implements EntityBaseFunction<PaySyncRecordDto> {

    /** 本地订单ID */
    @DbColumn(comment = "本地订单ID")
    private Long orderId;

    /** 本地业务号 */
    @DbColumn(comment = "本地业务号")
    private String orderNo;

    /** 网关订单号 */
    @DbColumn(comment = "网关订单号")
    private String gatewayOrderNo;

    /**
     * 同步类型 支付/退款
     * @see PaymentTypeEnum
     */
    @DbColumn(comment = "同步类型")
    private String syncType;

    /**
     * 同步的异步通道
     * @see PayChannelEnum#getCode()
     */
    @DbColumn(comment = "同步的异步通道")
    private String asyncChannel;

    /** 网关返回的同步消息 */
    @DbMySqlFieldType(MySqlFieldTypeEnum.LONGTEXT)
    @DbColumn(comment = "同步消息")
    private String syncInfo;

    /**
     * 网关返回状态
     * @see PaySyncStatusEnum
     * @see RefundSyncStatusEnum
     */
    @DbColumn(comment = "网关返回状态")
    private String gatewayStatus;

    /**
     * 支付单如果状态不一致, 是否进行修复
     */
    @DbColumn(comment = "是否进行修复")
    private boolean repairOrder;

    @DbColumn(comment = "修复单号")
    private String repairOrderNo;

    @DbColumn(comment = "错误消息")
    private String errorMsg;

    /** 客户端IP */
    @DbColumn(comment = "客户端IP")
    private String clientIp;

    /** 请求链路ID */
    @DbColumn(comment = "请求链路ID")
    private String reqId;

    /**
     * 转换
     */
    @Override
    public PaySyncRecordDto toDto() {
        return PaySyncRecordConvert.CONVERT.convert(this);
    }
}
