package com.taotao.cloud.payment.biz.daxpay.core.channel.voucher.dao;

import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xxm
 * @since 2022/3/14
 */
@Mapper
public interface VoucherMapper extends BaseMapper<Voucher> {

}
