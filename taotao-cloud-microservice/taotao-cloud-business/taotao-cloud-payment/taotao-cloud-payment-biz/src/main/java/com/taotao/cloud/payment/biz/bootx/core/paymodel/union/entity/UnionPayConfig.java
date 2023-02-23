package com.taotao.cloud.payment.biz.bootx.core.paymodel.union.entity;

import cn.bootx.common.core.function.EntityBaseFunction;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.payment.core.paymodel.union.convert.UnionPayConvert;
import cn.bootx.payment.dto.paymodel.union.UnionPayConfigDto;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
* 云闪付
* @author xxm
* @date 2022/3/11
*/
@Data
@Accessors(chain = true)
@TableName("pay_union_pay_config")
public class UnionPayConfig extends MpBaseEntity implements EntityBaseFunction<UnionPayConfigDto> {
    @Override
    public UnionPayConfigDto toDto() {
        return UnionPayConvert.CONVERT.convert(this);
    }
}
