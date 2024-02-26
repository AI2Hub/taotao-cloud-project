package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.pay.strategy;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayAmountAbnormalException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import com.taotao.cloud.payment.biz.daxpay.single.service.common.context.PayLocal;
import com.taotao.cloud.payment.biz.daxpay.single.service.common.local.PaymentContextLocal;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service.WeChatPayRecordService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service.WeChatPayService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.service.PayChannelOrderService;
import com.taotao.cloud.payment.biz.daxpay.single.service.func.AbsPayStrategy;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.wechat.WeChatPayParam;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付
 *
 * @author xxm
 * @since 2021/4/5
 */
@Scope(SCOPE_PROTOTYPE)
@Component
@RequiredArgsConstructor
public class WeChatPayStrategy extends AbsPayStrategy {

    private final PayChannelOrderService channelOrderService;

    private final WeChatPayConfigService weChatPayConfigService;

    private final WeChatPayService weChatPayService;

    private final WeChatPayRecordService weChatPayRecordService;

    private WeChatPayConfig weChatPayConfig;

    private WeChatPayParam weChatPayParam;

    /**
     * 类型
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 支付前操作
     */
    @Override
    public void doBeforePayHandler() {
        // 微信参数验证
        Map<String, Object> channelParam = this.getPayChannelParam().getChannelParam();
        if (CollUtil.isNotEmpty(channelParam)) {
            this.weChatPayParam = BeanUtil.toBean(channelParam, WeChatPayParam.class);
        } else {
            this.weChatPayParam = new WeChatPayParam();
        }

        // 检查金额
        PayChannelParam payMode = this.getPayChannelParam();
        if (payMode.getAmount() <= 0) {
            throw new PayAmountAbnormalException();
        }

        // 检查并获取微信支付配置
        this.initWeChatPayConfig();
        weChatPayService.validation(this.getPayChannelParam(), weChatPayConfig);
    }

    /**
     * 发起支付
     */
    @Override
    public void doPayHandler() {
        weChatPayService.pay(this.getOrder(), this.weChatPayParam, this.getPayChannelParam(), this.weChatPayConfig);
    }

    /**
     * 支付调起成功 , 保存或更新通道支付订单
     */
    @Override
    public void doSuccessHandler() {
        channelOrderService.switchAsyncPayChannel(this.getOrder(), this.getPayChannelParam());
        this.getOrder().setAsyncChannel(this.getChannel().getCode());
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();
        // 是否支付完成, 保存流水记录
        if (asyncPayInfo.isPayComplete()){
            weChatPayRecordService.pay(this.getOrder(), this.getChannelOrder());
        }
    }

    /**
     * 不使用默认的生成通道支付单方法, 异步支付通道的支付订单自己管理
     */
    @Override
    public void generateChannelOrder() {}

    /**
     * 初始化微信支付
     */
    private void initWeChatPayConfig() {
        this.weChatPayConfig = weChatPayConfigService.getAndCheckConfig();
    }

}
