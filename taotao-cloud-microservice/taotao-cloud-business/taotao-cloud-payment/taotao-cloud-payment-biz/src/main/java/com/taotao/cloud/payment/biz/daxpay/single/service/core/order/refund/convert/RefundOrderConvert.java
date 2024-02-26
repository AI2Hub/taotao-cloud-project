package com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.convert;

import cn.bootx.platform.daxpay.result.order.RefundOrderResult;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.refund.entity.RefundOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.order.refund.RefundOrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @since 2022/3/2
 */
@Mapper
public interface RefundOrderConvert {

    RefundOrderConvert CONVERT = Mappers.getMapper(RefundOrderConvert.class);

    RefundOrderDto convert(RefundOrder in);

    RefundOrderResult convertResult(RefundOrder in);


}
