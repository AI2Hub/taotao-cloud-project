package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.service;

import com.taotao.cloud.payment.biz.bootx.code.pay.PayStatusCode;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao.VoucherPaymentManager;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.entity.VoucherPayment;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayModeParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 储值卡支付记录
 * @author xxm
 * @date 2022/3/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class VoucherPaymentService {
    private final VoucherPaymentManager voucherPaymentManager;

    /**
     * 添加支付记录
     */
    public void savePayment(Payment payment, PayParam payParam, PayModeParam payMode, List<Voucher> vouchers){
        String voucherIds = vouchers.stream().map(MpIdEntity::getId).map(String::valueOf).collect(Collectors.joining(","));

        VoucherPayment walletPayment = new VoucherPayment()
                .setVoucherIds(voucherIds);
        walletPayment.setPaymentId(payment.getId())
                .setUserId(payment.getUserId())
                .setBusinessId(payParam.getBusinessId())
                .setAmount(payMode.getAmount())
                .setRefundableBalance(payMode.getAmount())
                .setPayStatus(payment.getPayStatus());
        voucherPaymentManager.save(walletPayment);
    }

    /**
     * 更新成功状态
     */
    public void updateSuccess(Long paymentId) {
        Optional<VoucherPayment> payment = voucherPaymentManager.findByPaymentId(paymentId);
        if (payment.isPresent()){
            VoucherPayment voucherPayment = payment.get();
            voucherPayment.setPayStatus(PayStatusCode.TRADE_SUCCESS)
                    .setPayTime(LocalDateTime.now());
            voucherPaymentManager.updateById(voucherPayment);
        }
    }

    /**
     * 关闭操作
     */
    public void updateClose(Long paymentId) {
        VoucherPayment payment = voucherPaymentManager.findByPaymentId(paymentId)
                .orElseThrow(() -> new BizException("未查询到查询交易记录"));
        payment.setPayStatus(PayStatusCode.TRADE_CANCEL);
        voucherPaymentManager.updateById(payment);
    }

    /**
     * 更新退款
     */
    public void updateRefund(Long paymentId, BigDecimal amount){
        Optional<VoucherPayment> voucherPayment = voucherPaymentManager.findByPaymentId(paymentId);
        voucherPayment.ifPresent(payment -> {
            BigDecimal refundableBalance = payment.getRefundableBalance().subtract(amount);
            payment.setRefundableBalance(refundableBalance);
            if (BigDecimalUtil.compareTo(refundableBalance, BigDecimal.ZERO)==0){
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDED);
            } else {
                payment.setPayStatus(PayStatusCode.TRADE_REFUNDING);
            }
            voucherPaymentManager.updateById(payment);
        });
    }
}
