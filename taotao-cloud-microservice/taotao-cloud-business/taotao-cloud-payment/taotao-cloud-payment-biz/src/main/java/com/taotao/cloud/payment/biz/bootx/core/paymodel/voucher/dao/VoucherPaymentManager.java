package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.daxpay.core.paymodel.voucher.entity.VoucherPayment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author xxm
 * @date 2022/3/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherPaymentManager extends BaseManager<VoucherPaymentMapper, VoucherPayment> {

    /**
     * 根据支付id
     */
    public Optional<VoucherPayment> findByPaymentId(Long paymentId) {
        return this.findByField(VoucherPayment::getPaymentId, paymentId);
    }

}
