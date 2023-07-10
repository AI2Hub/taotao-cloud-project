package com.taotao.cloud.payment.biz.daxpay.core.merchant.entity;

import cn.bootx.mybatis.table.modify.annotation.DbColumn;
import cn.bootx.mybatis.table.modify.annotation.DbTable;
import cn.bootx.mybatis.table.modify.mybatis.mysq.annotation.DbMySqlIndex;
import cn.bootx.mybatis.table.modify.mybatis.mysq.constants.MySqlIndexType;
import cn.bootx.platform.common.core.function.EntityBaseFunction;
import cn.bootx.platform.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.platform.daxpay.core.merchant.convert.MerchantInfoConvert;
import cn.bootx.platform.daxpay.dto.merchant.MerchantInfoDto;
import cn.bootx.platform.daxpay.param.merchant.MerchantInfoParam;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import static cn.bootx.platform.daxpay.core.merchant.entity.MerchantInfo.Fields.code;

/**
 * 商户
 *
 * @author xxm
 * @since 2023-05-17
 */
@DbMySqlIndex(fields = code, type = MySqlIndexType.UNIQUE, comment = "商户号唯一索引")
@EqualsAndHashCode(callSuper = true)
@FieldNameConstants
@Data
@DbTable(comment = "商户")
@Accessors(chain = true)
@TableName("pay_merchant")
public class MerchantInfo extends MpBaseEntity implements EntityBaseFunction<MerchantInfoDto> {

    /** 商户号 */
    @DbColumn(comment = "商户号")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String code;

    /** 商户名称 */
    @DbColumn(comment = "商户名称")
    private String name;

    /** 商户简称 */
    @DbColumn(comment = "商户简称")
    private String shortName;

    /** 类型 */
    @DbColumn(comment = "类型")
    private String type;

    /** 联系人姓名 */
    @DbColumn(comment = "联系人姓名")
    private String contactName;

    /** 联系人手机号 */
    @DbColumn(comment = "联系人手机号")
    private String contactTel;

    /**
     * 状态类型
     * @see cn.bootx.platform.daxpay.code.MchAndAppCode
     */
    @DbColumn(comment = "状态类型")
    private String state;

    /** 商户备注 */
    @DbColumn(comment = "商户备注")
    private String remark;

    /** 创建对象 */
    public static MerchantInfo init(MerchantInfoParam in) {
        return MerchantInfoConvert.CONVERT.convert(in);
    }

    /** 转换成dto */
    @Override
    public MerchantInfoDto toDto() {
        return MerchantInfoConvert.CONVERT.convert(this);
    }

}
