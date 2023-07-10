package com.taotao.cloud.payment.biz.daxpay.core.refund.record.convert;

import cn.bootx.platform.daxpay.core.refund.record.entity.RefundRecord;
import cn.bootx.platform.daxpay.dto.refund.RefundRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface RefundConvert {

    RefundConvert CONVERT = Mappers.getMapper(RefundConvert.class);

    RefundRecordDto convert(RefundRecord in);

}
