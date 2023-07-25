package com.taotao.cloud.payment.biz.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 商户应用支付配置
 *
 * @author xxm
 * @since 2023-05-19
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@DbTable(comment = "商户应用支付配置")
@Accessors(chain = true)
@TableName("pay_mch_app_config")
public class MchAppPayConfig extends MpBaseEntity {

    /** 关联应用编码 */
    @DbMySqlIndex(comment = "关联商户应用编码索引")
    @DbColumn(comment = "关联商户应用编码")
    private String appCode;

    /** 关联配置ID */
    @DbColumn(comment = "关联配置ID")
    private Long configId;

    /** 支付通道编码 */
    @DbColumn(comment = "支付通道编码")
    private String channel;

    /** 状态 */
    @DbColumn(comment = "状态")
    private String state;

}
