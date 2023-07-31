package com.taotao.cloud.payment.biz.daxpay.core.sync.record.dao;

import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.core.sync.record.entity.PaySyncRecord;
import cn.bootx.platform.daxpay.dto.sync.PaySyncRecordDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 *
 * @author xxm
 * @since 2023/7/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PaySyncRecordManager extends BaseManager<PaySyncRecordMapper, PaySyncRecord> {

    public Page<PaySyncRecord> page(PageParam pageParam, PaySyncRecordDto param) {
        Page<PaySyncRecord> mpPage = MpUtil.getMpPage(pageParam, PaySyncRecord.class);
        return lambdaQuery().orderByDesc(MpIdEntity::getId)
                .like(Objects.nonNull(param.getPaymentId()), PaySyncRecord::getPaymentId, param.getPaymentId())
                .eq(Objects.nonNull(param.getPayChannel()), PaySyncRecord::getPayChannel, param.getPayChannel())
                .eq(Objects.nonNull(param.getStatus()), PaySyncRecord::getStatus, param.getStatus())
                .page(mpPage);
    }

}
