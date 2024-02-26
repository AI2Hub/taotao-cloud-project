package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.channel.AliPayParam;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import com.taotao.cloud.payment.biz.daxpay.single.service.common.context.PayLocal;
import com.taotao.cloud.payment.biz.daxpay.single.service.common.local.PaymentContextLocal;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.entity.AliPayConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.service.AliPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.service.AliPayRecordService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.alipay.service.AliPayService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.service.PayChannelOrderService;
import com.taotao.cloud.payment.biz.daxpay.single.service.func.AbsPayStrategy;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    private final PayChannelOrderService channelOrderService;

    private final AliPayService aliPayService;

    private final AliPayConfigService alipayConfigService;

    private final AliPayRecordService aliRecordService;

    private AliPayConfig alipayConfig;

    private AliPayParam aliPayParam;

    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.ALI;
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        try {
            // 支付宝参数验证
            Map<String, Object> channelParam = this.getPayChannelParam().getChannelParam();
            if (CollUtil.isNotEmpty(channelParam)) {
                this.aliPayParam = BeanUtil.toBean(channelParam, AliPayParam.class);
            }
            else {
                this.aliPayParam = new AliPayParam();
            }
        }
        catch (JSONException e) {
            throw new PayFailureException("支付参数错误");
        }
        // 检查金额
        PayChannelParam payMode = this.getPayChannelParam();
        if (payMode.getAmount() <= 0) {
            throw new PayAmountAbnormalException();
        }
        // 检查并获取支付宝支付配置
        this.initAlipayConfig();
        aliPayService.validation(this.getPayChannelParam(), alipayConfig);
    }

    /**
     * 发起支付操作
     */
    @Override
    public void doPayHandler() {
        aliPayService.pay(this.getOrder(), this.getPayChannelParam(), this.aliPayParam, this.alipayConfig);
    }

    /**
     * 不使用默认的生成通道支付单方法, 异步支付通道的支付订单自己管理
     * channelOrderService.switchAsyncPayChannel 进行切换
     */
    @Override
    public void generateChannelOrder() {
    }

    /**
     * 支付调起成功, 保存或更新通道支付订单
     */
    @Override
    public void doSuccessHandler() {
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();
        channelOrderService.switchAsyncPayChannel(this.getOrder(), this.getPayChannelParam());
        // 支付完成, 保存记录
        if (asyncPayInfo.isPayComplete()) {
            aliRecordService.pay(this.getOrder(), this.getChannelOrder());
        }
    }

    /**
     * 初始化支付宝配置信息
     */
    private void initAlipayConfig() {
        // 获取并初始化支付宝支付配置
        this.alipayConfig = alipayConfigService.getAndCheckConfig();
        alipayConfigService.initConfig(this.alipayConfig);
    }

}
