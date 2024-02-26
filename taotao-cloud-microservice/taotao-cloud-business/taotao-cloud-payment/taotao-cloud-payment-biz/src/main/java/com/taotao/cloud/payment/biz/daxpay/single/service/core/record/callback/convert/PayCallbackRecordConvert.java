package com.taotao.cloud.payment.biz.daxpay.single.service.core.record.callback.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.record.callback.entity.PayCallbackRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.callback.PayCallbackRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 回调通知转换
 * @author xxm
 * @since 2023/12/18
 */
@Mapper
public interface PayCallbackRecordConvert {
    PayCallbackRecordConvert CONVERT = Mappers.getMapper(PayCallbackRecordConvert.class);

    PayCallbackRecordDto convert(PayCallbackRecord in);
}
