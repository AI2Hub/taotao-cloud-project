package com.taotao.cloud.payment.biz.daxpay.single.service.core.record.sync.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.record.sync.entity.PaySyncRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.sync.PaySyncRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付同步记录同步
 * @author xxm
 * @since 2023/7/14
 */
@Mapper
public interface PaySyncRecordConvert {
    PaySyncRecordConvert CONVERT = Mappers.getMapper(PaySyncRecordConvert.class);

    PaySyncRecordDto convert(PaySyncRecord in);

}
