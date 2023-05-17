package com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.convert;

import cn.bootx.daxpay.core.paymodel.alipay.entity.AlipayConfig;
import cn.bootx.daxpay.dto.paymodel.alipay.AlipayConfigDto;
import cn.bootx.daxpay.param.paymodel.alipay.AlipayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 支付宝转换
 *
 * @author xxm
 * @date 2021/7/5
 */
@Mapper
public interface AlipayConvert {

    AlipayConvert CONVERT = Mappers.getMapper(AlipayConvert.class);

    AlipayConfig convert(AlipayConfigDto in);

    AlipayConfig convert(AlipayConfigParam in);

    AlipayConfigDto convert(AlipayConfig in);

}
