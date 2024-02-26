package com.taotao.cloud.payment.biz.daxpay.single.service.core.record.repair.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.record.repair.entity.PayRepairRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.repair.PayRepairRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/1/6
 */
@Mapper
public interface PayRepairRecordConvert {
    PayRepairRecordConvert CONVERT = Mappers.getMapper(PayRepairRecordConvert.class);

    PayRepairRecordDto convert(PayRepairRecord in);
}
