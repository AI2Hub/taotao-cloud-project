package com.taotao.cloud.payment.biz.daxpay.core.channel.voucher.convert;

import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.VoucherPayment;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherDto;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherPaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author xxm
 * @date 2022/3/14
 */
@Mapper
public interface VoucherConvert {

    VoucherConvert CONVERT = Mappers.getMapper(VoucherConvert.class);

    VoucherDto convert(Voucher in);

    VoucherPaymentDto convert(VoucherPayment in);

}
