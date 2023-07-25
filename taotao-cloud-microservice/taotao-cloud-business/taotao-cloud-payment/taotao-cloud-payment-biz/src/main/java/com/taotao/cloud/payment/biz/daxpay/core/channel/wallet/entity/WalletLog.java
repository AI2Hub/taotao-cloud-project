package com.taotao.cloud.payment.biz.daxpay.core.channel.wallet.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.channel.wallet.convert.WalletConvert;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletLogDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 钱包日志
 *
 * @author xxm
 * @since 2020/12/8
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment = "钱包日志")
@Accessors(chain = true)
@TableName("pay_wallet_log")
public class WalletLog extends MpBaseEntity implements EntityBaseFunction<WalletLogDto> {

    /** 钱包id */
    @DbMySqlIndex(comment = "钱包索引ID")
    @DbColumn(comment = "钱包id")
    private Long walletId;

    /** 用户id */
    @DbColumn(comment = "用户id")
    private Long userId;

    /** 类型 */
    @DbColumn(comment = "类型")
    private String type;

    /** 交易记录ID */
    @DbColumn(comment = "交易记录ID")
    private Long paymentId;

    /** 业务ID */
    @DbColumn(comment = "业务ID")
    private String businessId;

    /**
     * 操作类型
     * @see cn.bootx.platform.daxpay.code.paymodel.WalletCode#OPERATION_SOURCE_USER
     */
    @DbColumn(comment = "操作类型")
    private String operationSource;

    /** 金额 */
    @DbColumn(comment = "金额")
    private BigDecimal amount;

    /** 备注 */
    @DbColumn(comment = "备注")
    private String remark;


    @Override
    public WalletLogDto toDto() {
        return WalletConvert.CONVERT.convert(this);
    }

}
