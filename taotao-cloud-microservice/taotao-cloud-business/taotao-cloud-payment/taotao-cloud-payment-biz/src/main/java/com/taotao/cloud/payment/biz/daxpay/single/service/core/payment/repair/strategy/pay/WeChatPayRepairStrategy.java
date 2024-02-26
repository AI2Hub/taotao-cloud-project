package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.repair.strategy.pay;

import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.code.PayStatusEnum;
import com.taotao.cloud.payment.biz.daxpay.single.service.common.local.PaymentContextLocal;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service.WeChatPayCloseService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service.WeChatPayRecordService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayChannelOrderManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.func.AbsPayRepairStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 微信支付订单修复策略类
 * @author xxm
 * @since 2023/12/29
 */
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@Service
@RequiredArgsConstructor
public class WeChatPayRepairStrategy extends AbsPayRepairStrategy {
    private final WeChatPayCloseService closeService;

    private final WeChatPayConfigService weChatPayConfigService;

    private final PayChannelOrderManager payChannelOrderManager;

    private final WeChatPayRecordService weChatPayRecordService;

    private WeChatPayConfig weChatPayConfig;

    /**
     * 策略标识
     */
    @Override
    public PayChannelEnum getChannel() {
        return PayChannelEnum.WECHAT;
    }

    /**
     * 修复前处理
     */
    @Override
    public void doBeforeHandler() {
        this.weChatPayConfig = weChatPayConfigService.getConfig();
    }

    /**
     * 支付成功处理
     */
    @Override
    public void doPaySuccessHandler() {
        LocalDateTime payTime = PaymentContextLocal.get()
                .getRepairInfo()
                .getFinishTime();
        this.getChannelOrder().setStatus(PayStatusEnum.SUCCESS.getCode())
                .setPayTime(payTime);
        payChannelOrderManager.updateById(this.getChannelOrder());
        // 保存流水记录
        weChatPayRecordService.pay(this.getOrder(), this.getChannelOrder());
    }

    /**
     * 关闭本地支付
     */
    @Override
    public void doCloseLocalHandler() {
        this.getChannelOrder().setStatus(PayStatusEnum.CLOSE.getCode());
    }

    /**
     * 关闭本地支付和网关支付
     */
    @Override
    public void doCloseGatewayHandler() {
        closeService.close(this.getOrder(),this.weChatPayConfig);
        this.doCloseLocalHandler();
    }
}
