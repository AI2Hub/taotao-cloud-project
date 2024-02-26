package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.pay.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.util.LocalDateTimeUtil;
import cn.bootx.platform.daxpay.code.PayChannelEnum;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.pay.PayChannelParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import com.taotao.cloud.payment.biz.daxpay.single.service.common.context.*;
import com.taotao.cloud.payment.biz.daxpay.single.service.common.local.PaymentContextLocal;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.builder.PayBuilder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayChannelOrderManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.dao.PayOrderExtraManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayChannelOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrder;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.entity.PayOrderExtra;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.service.PayOrderQueryService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.order.pay.service.PayOrderService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.sync.service.PaySyncService;
import com.taotao.cloud.payment.biz.daxpay.single.service.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.util.PayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.PayStatusEnum.*;

/**
 * 支付支持服务
 * @author xxm
 * @since 2023/12/24
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PayAssistService {

    private final PaySyncService paySyncService;

    private final PayOrderService payOrderService;

    private final PayOrderQueryService payOrderQueryService;

    private final PayOrderExtraManager payOrderExtraManager;

    private final PayChannelOrderManager payChannelOrderManager;

    /**
     * 初始化支付相关上下文
     */
    public void initPayContext(PayOrder order, PayParam payParam){
        // 初始化支付订单超时时间
        this.initExpiredTime(order,payParam);
        // 初始化通知相关上下文
        this.initNotice(payParam);
    }


    /**
     * 初始化支付订单超时时间
     * 1. 如果支付记录为空, 超时时间读取顺序 PayParam -> 平台设置
     * 2. 如果支付记录不为空, 超时时间通过支付记录进行反推
     */
    public void initExpiredTime(PayOrder order, PayParam payParam){
        // 不是异步支付，没有超时时间
        if (PayUtil.isNotSync(payParam.getPayChannels())){
            return;
        }
        PayLocal asyncPayInfo = PaymentContextLocal.get().getPayInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        // 支付订单是非为空
        if (Objects.nonNull(order)){
            asyncPayInfo.setExpiredTime(order.getExpiredTime());
            return;
        }
        // 支付参数传入
        if (Objects.nonNull(payParam.getExpiredTime())){
            asyncPayInfo.setExpiredTime(payParam.getExpiredTime());
            return;
        }
        // 读取本地时间
        LocalDateTime paymentExpiredTime = PayUtil.getPaymentExpiredTime(platform.getOrderTimeout());
        asyncPayInfo.setExpiredTime(paymentExpiredTime);
    }

    /**
     * 初始化通知相关上下文
     */
    private void initNotice(PayParam payParam){
        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
        PlatformLocal platform = PaymentContextLocal.get().getPlatformInfo();
        // 异步回调为开启状态
        if (!payParam.isNotNotify() && apiInfo.isNotice()){
            // 首先读取请求参数
            noticeInfo.setNotifyUrl(payParam.getNotifyUrl());
            // 读取接口配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())){
                noticeInfo.setNotifyUrl(apiInfo.getNoticeUrl());
            }
            // 读取平台配置
            if (StrUtil.isBlank(noticeInfo.getNotifyUrl())){
                noticeInfo.setNotifyUrl(platform.getNotifyUrl());
            }
        }
        // 同步回调
        noticeInfo.setReturnUrl(payParam.getReturnUrl());
        if (StrUtil.isBlank(noticeInfo.getReturnUrl())){
            noticeInfo.setReturnUrl(platform.getNotifyUrl());
        }
        // 退出回调地址
        noticeInfo.setQuitUrl(payParam.getQuitUrl());
    }


    /**
     * 获取异步支付参数
     */
    public PayChannelParam getAsyncPayParam(PayParam payParam, PayOrder payOrder) {
        // 查询之前的支付方式
        String asyncPayChannel = payOrder.getAsyncChannel();
        PayChannelOrder payChannelOrder = payChannelOrderManager.findByPaymentIdAndChannel(payOrder.getId(), asyncPayChannel)
                .orElseThrow(() -> new PayFailureException("支付方式数据异常"));

        // 新的异步支付方式
        PayChannelParam payChannelParam = payParam.getPayChannels()
                .stream()
                .filter(payMode -> PayChannelEnum.ASYNC_TYPE_CODE.contains(payMode.getChannel()))
                .findFirst()
                .orElseThrow(() -> new PayFailureException("支付方式数据异常"));
        // 新传入的金额是否一致
        if (!Objects.equals(payChannelOrder.getAmount(), payChannelParam.getAmount())){
            throw new PayFailureException("传入的支付金额非法！与订单金额不一致");
        }
        return payChannelParam;
    }

    /**
     * 创建支付订单并保存, 返回支付订单
     */
    public PayOrder createPayOrder(PayParam payParam) {
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        // 构建支付订单并保存
        PayOrder order = PayBuilder.buildPayOrder(payParam);
        payOrderService.save(order);
        // 构建支付订单扩展表并保存
        PayOrderExtra payOrderExtra = PayBuilder.buildPayOrderExtra(payParam, order.getId());
        payOrderExtraManager.save(payOrderExtra);
        payInfo.setPayOrder(order).setPayOrderExtra(payOrderExtra);
        return order;
    }

    /**
     * 创建并保存通道支付订单
     */
    public void createPayChannelOrder(List<AbsPayStrategy> payStrategies) {
        PayLocal payInfo = PaymentContextLocal.get().getPayInfo();
        List<PayChannelOrder> channelOrders = payStrategies.stream()
                .map(AbsPayStrategy::getChannelOrder)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        payChannelOrderManager.saveAll(channelOrders);
        payInfo.getPayChannelOrders().addAll(channelOrders);
    }

    /**
     * 更新支付订单扩展参数
     * @param payParam 支付参数
     * @param paymentId 支付订单id
     */
    public PayOrderExtra updatePayOrderExtra(PayParam payParam,Long paymentId){
        ApiInfoLocal apiInfo = PaymentContextLocal.get().getApiInfo();
        RequestLocal requestInfo = PaymentContextLocal.get().getRequestInfo();
        PlatformLocal platformInfo = PaymentContextLocal.get().getPlatformInfo();
        PayOrderExtra payOrderExtra = payOrderExtraManager.findById(paymentId)
                .orElseThrow(() -> new DataNotExistException("支付订单不存在"));

        NoticeLocal noticeInfo = PaymentContextLocal.get().getNoticeInfo();
        String notifyUrl = noticeInfo.getNotifyUrl();
        String returnUrl = noticeInfo.getReturnUrl();

        payOrderExtra.setReqTime(payParam.getReqTime())
                .setReqSignType(platformInfo.getSignType())
                .setReqSign(payParam.getSign())
                .setNotifyUrl(notifyUrl)
                .setReturnUrl(returnUrl)
                .setNoticeSign(apiInfo.isNoticeSign())
                .setAttach(payParam.getAttach())
                .setClientIp(payParam.getClientIp())
                .setReqId(requestInfo.getReqId());
        return payOrderExtraManager.updateById(payOrderExtra);
    }

    /**
     * 校验支付状态，支付成功则返回，支付失败则抛出对应的异常
     */
    public PayOrder getOrderAndCheck(String businessNo) {
        // 根据订单查询支付记录
        PayOrder payOrder = payOrderQueryService.findByBusinessNo(businessNo).orElse(null);
        if (Objects.nonNull(payOrder)) {
            // 待支付
            if (Objects.equals(payOrder.getStatus(), PROGRESS.getCode())){
                // 如果支付超时, 触发订单同步操作, 同时抛出异常
                if (Objects.nonNull(payOrder.getExpiredTime()) && LocalDateTimeUtil.ge(LocalDateTime.now(), payOrder.getExpiredTime())) {
                    paySyncService.syncPayOrder(payOrder);
                    throw new PayFailureException("支付已超时，请重新确认支付状态");
                }
                return payOrder;
            }
            // 已经支付状态
            if (SUCCESS.getCode().equals(payOrder.getStatus())) {
                throw new PayFailureException("已经支付成功，请勿重新支付");
            }
            // 支付失败类型状态
            List<String> tradesStatus = Arrays.asList(FAIL.getCode(), CLOSE.getCode());
            if (tradesStatus.contains(payOrder.getStatus())) {
                throw new PayFailureException("支付失败或已经被关闭");
            }
            // 退款类型状态
            tradesStatus = Arrays.asList(REFUNDED.getCode(), PARTIAL_REFUND.getCode(), REFUNDING.getCode());
            if (tradesStatus.contains(payOrder.getStatus())) {
                throw new PayFailureException("该订单处于退款状态");
            }
            // 其他状态直接抛出兜底异常
            throw new PayFailureException("订单不是待支付状态，请重新确认订单状态");
        }
        return null;
    }
}
