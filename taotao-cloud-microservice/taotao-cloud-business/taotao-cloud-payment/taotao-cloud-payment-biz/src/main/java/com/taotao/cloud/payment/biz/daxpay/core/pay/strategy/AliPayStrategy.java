package com.taotao.cloud.payment.biz.daxpay.core.pay.strategy;

import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.code.paymodel.AliPayCode;
import cn.bootx.platform.daxpay.core.channel.alipay.dao.AlipayConfigManager;
import cn.bootx.platform.daxpay.core.channel.alipay.entity.AlipayConfig;
import cn.bootx.platform.daxpay.core.channel.alipay.service.*;
import cn.bootx.platform.daxpay.core.pay.exception.ExceptionInfo;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.pay.result.PaySyncResult;
import cn.bootx.platform.daxpay.exception.payment.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.alipay.AliPayParam;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 支付宝支付
 *
 * @author xxm
 * @since 2021/2/27
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class AliPayStrategy extends AbsPayStrategy {

    private final AlipayConfigManager alipayConfigManager;

    private final AliPaymentService aliPaymentService;

    private final AlipaySyncService alipaySyncService;

    private final AliPayService aliPayService;

    private final AlipayConfigService alipayConfigService;

    private final AliPayCancelService aliPayCancelService;

    private AlipayConfig alipayConfig;

    private AliPayParam aliPayParam;

    @Override
    public PayChannelEnum getType() {
        return PayChannelEnum.ALI;
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            String extraParamsJson = this.getPayWayParam().getExtraParamsJson();
            if (StrUtil.isNotBlank(extraParamsJson)) {
                this.aliPayParam = JSONUtil.toBean(extraParamsJson, AliPayParam.class);
            }
            else {
                this.aliPayParam = new AliPayParam();
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }
        // 检查金额
        PayWayParam payMode = this.getPayWayParam();
        if (BigDecimalUtil.compareTo(payMode.getAmount(), BigDecimal.ZERO) < 1) {
            throw new PayAmountAbnormalException();
        }
        // 检查并获取支付宝支付配置
        this.initAlipayConfig(this.getPayParam().getMchAppCode());
        aliPayService.validation(this.getPayWayParam(), alipayConfig);
        // 如果没有显式传入同步回调地址, 使用默认配置
        if (StrUtil.isBlank(aliPayParam.getReturnUrl())) {
            aliPayParam.setReturnUrl(alipayConfig.getReturnUrl());
        }
        this.initAlipayConfig(this.getPayParam().getMchAppCode());
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        aliPayService.pay(this.getPayWayParam().getAmount(), this.getPayment(), this.aliPayParam, this.getPayWayParam(),
                this.alipayConfig);
    }

    /**
     * 支付调起成功
     */
    @Override
    public void doSuccessHandler() {
        aliPaymentService.updatePaySuccess(this.getPayment(), this.getPayWayParam());
    }

    /**
     * 发起支付失败
     */
    @Override
    public void doErrorHandler(ExceptionInfo exceptionInfo) {
        this.doCloseHandler();
    }

    /**
     * 异步支付成功
     */
    @Override
    public void doAsyncSuccessHandler(Map<String, String> map) {
        String tradeNo = map.get(AliPayCode.TRADE_NO);
        aliPaymentService.updateAsyncSuccess(this.getPayment().getId(), this.getPayWayParam(), tradeNo);
    }

    /**
     * 异步支付失败
     */
    @Override
    public void doAsyncErrorHandler(ExceptionInfo exceptionInfo) {
        // 调用撤销支付
        this.doCancelHandler();
    }

    /**
     * 撤销支付
     */
    @Override
    public void doCancelHandler() {
        this.initAlipayConfig(this.getPayParam().getMchAppCode());
        // 撤销支付
        aliPayCancelService.cancelRemote(this.getPayment());
        // 调用关闭本地支付记录
        this.doCloseHandler();
    }

    /**
     * 关闭本地支付记录
     */
    @Override
    public void doCloseHandler() {
        aliPaymentService.updateClose(this.getPayment().getId());
    }

    /**
     * 异步支付单与支付网关进行状态比对
     */
    @Override
    public PaySyncResult doSyncPayStatusHandler() {
        this.initAlipayConfig(this.getPayParam().getMchAppCode());
        return alipaySyncService.syncPayStatus(this.getPayment());
    }

    /**
     * 初始化支付宝配置信息
     */
    private void initAlipayConfig(String mchAppCode) {
        // 检查并获取支付宝支付配置
        this.alipayConfig = alipayConfigManager.findByMchAppCode(mchAppCode)
                .orElseThrow(() -> new PayFailureException("支付配置不存在"));
        alipayConfigService.initApiConfig(this.alipayConfig);
    }

}
