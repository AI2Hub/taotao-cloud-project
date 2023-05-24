package com.taotao.cloud.payment.biz.daxpay.core.merchant.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.common.query.generator.QueryGenerator;
import cn.bootx.platform.daxpay.core.merchant.entity.MerchantInfo;
import cn.bootx.platform.daxpay.param.merchant.MerchantInfoParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 商户
 * @author xxm
 * @date 2023-05-17
 */
@Repository
@RequiredArgsConstructor
public class MerchantInfoManager extends BaseManager<MerchantInfoMapper, MerchantInfo> {

    /**
    * 分页
    */
    public Page<MerchantInfo> page(PageParam pageParam, MerchantInfoParam param) {
        Page<MerchantInfo> mpPage = MpUtil.getMpPage(pageParam, MerchantInfo.class);
        QueryWrapper<MerchantInfo> wrapper = QueryGenerator.generator(param, this.getEntityClass());
        wrapper.select(this.getEntityClass(),MpUtil::excludeBigField)
                .orderByDesc(MpUtil.getColumnName(MerchantInfo::getId));
        return this.page(mpPage,wrapper);
    }
}
