package com.taotao.cloud.payment.biz.daxpay.core.channel.voucher.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import cn.bootx.platform.daxpay.code.paymodel.VoucherCode;
import cn.bootx.platform.daxpay.core.channel.voucher.dao.VoucherManager;
import cn.bootx.platform.daxpay.core.channel.voucher.entity.Voucher;
import cn.bootx.platform.daxpay.dto.channel.voucher.VoucherDto;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.voucher.VoucherParam;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 储值卡查询
 *
 * @author xxm
 * @since 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherQueryService {

    private final VoucherManager voucherManager;

    /**
     * 分页
     */
    public PageResult<VoucherDto> page(PageParam pageParam, VoucherParam param) {
        return MpUtil.convert2DtoPageResult(voucherManager.page(pageParam, param));
    }

    /**
     * 根据id查询
     */
    public VoucherDto findById(Long id) {
        return voucherManager.findById(id).map(Voucher::toDto).orElseThrow(() -> new DataNotExistException("储值卡不存在"));
    }

    /**
     * 根据卡号查询
     */
    public VoucherDto findByCardNo(String cardNo) {
        return voucherManager.findByCardNo(cardNo)
                .map(Voucher::toDto)
                .orElseThrow(() -> new DataNotExistException("储值卡不存在"));
    }

    /**
     * 获取并判断卡状态
     */
    public VoucherDto getAndJudgeVoucher(String cardNo){
        Voucher voucher = voucherManager.findByCardNo(cardNo)
                .orElseThrow(() -> new DataNotExistException("储值卡不存在"));
        // 过期
        String checkMsg = check(Collections.singletonList(voucher));
        if (StrUtil.isNotBlank(checkMsg)){
            throw new PayFailureException(checkMsg);
        }
        return voucher.toDto();
    }

    /**
     * 卡信息检查
     */
    public String check(List<Voucher> vouchers) {
        // 判断有效期
        boolean timeCheck = vouchers.stream()
                .filter(voucher -> !Objects.equals(voucher.isEnduring(), true))
                .allMatch(voucher -> LocalDateTimeUtil.between(LocalDateTime.now(), voucher.getStartTime(),
                        voucher.getEndTime()));
        if (!timeCheck) {
            return "储值卡不再有效期内";
        }
        // 判断状态
        boolean statusCheck = vouchers.stream()
                .allMatch(voucher -> Objects.equals(voucher.getStatus(), VoucherCode.STATUS_NORMAL));
        if (!statusCheck){
            return "储值卡不是启用状态";
        }
        return null;
    }

}
