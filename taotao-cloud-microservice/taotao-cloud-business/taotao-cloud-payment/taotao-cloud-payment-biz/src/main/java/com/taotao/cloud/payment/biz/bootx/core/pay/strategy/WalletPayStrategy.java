package com.taotao.cloud.payment.biz.bootx.core.pay.strategy;

import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelCode;
import com.taotao.cloud.payment.biz.bootx.code.pay.PayChannelEnum;
import com.taotao.cloud.payment.biz.bootx.core.pay.func.AbsPayStrategy;
import com.taotao.cloud.payment.biz.bootx.core.payment.service.PaymentService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.entity.Wallet;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service.WalletPayService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service.WalletPaymentService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service.WalletService;
import com.taotao.cloud.payment.biz.bootx.exception.waller.WalletLackOfBalanceException;
import com.taotao.cloud.payment.biz.bootx.param.pay.PayParam;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**   
* 钱包支付策略
* @author xxm  
* @date 2020/12/11 
*/
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WalletPayStrategy extends AbsPayStrategy {

    private final WalletPaymentService walletPaymentService;
    private final WalletPayService walletPayService;
    private final WalletService walletService;
    private final PaymentService paymentService;

    private Wallet wallet;

    @Override
    public int getType() {
        return PayChannelCode.WALLET;
    }

    /**
     * 支付前处理
     */
    @Override
    public void doBeforePayHandler() {
        PayParam payParam = this.getPayParam();
        // 获取并校验钱包
        this.wallet = walletService.getNormalWalletByUserId(payParam.getUserId());
        // 判断余额
        if (BigDecimalUtil.compareTo(this.wallet.getBalance(),getPayMode().getAmount()) < 0) {
            throw new WalletLackOfBalanceException();
        }
    }

    /**
     * 支付操作
     */
    @Override
    public void doPayHandler() {
        walletPayService.pay(getPayMode().getAmount(),
                this.getPayment(),
                this.wallet);
        walletPaymentService.savePayment(this.getPayment(), this.getPayParam(),this.getPayMode(),this.wallet);
    }

    /**
     * 成功
     */
    @Override
    public void doSuccessHandler() {
        walletPaymentService.updateSuccess(this.getPayment().getId());
    }

    /**
     * 取消支付并返还金额
     */
    @Override
    public void doCloseHandler() {
        walletPayService.close(this.getPayment().getId());
        walletPaymentService.updateClose(this.getPayment().getId());
    }

    /**
     * 退款
     */
    @Override
    public void doRefundHandler() {
        walletPayService.refund(this.getPayment().getId(),this.getPayMode().getAmount());
        walletPaymentService.updateRefund(this.getPayment().getId(),this.getPayMode().getAmount());
        paymentService.updateRefundSuccess(this.getPayment(),this.getPayMode().getAmount(), PayChannelEnum.WALLET);
    }

}
