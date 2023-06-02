package com.taotao.cloud.payment.biz.daxpay.core.pay.factory;

import cn.bootx.platform.daxpay.code.pay.PayChannelEnum;
import cn.bootx.platform.daxpay.core.pay.func.AbsPayStrategy;
import cn.bootx.platform.daxpay.core.pay.strategy.*;
import cn.bootx.platform.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.pay.PayWayParam;
import org.dromara.hutool.core.collection.CollectionUtil;
import org.dromara.hutool.extra.spring.SpringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.bootx.platform.daxpay.code.pay.PayChannelEnum.*;

/**
 * 支付策略工厂
 *
 * @author xxm
 * @date 2020/12/11
 */
public class PayStrategyFactory {

    /**
     * 根据传入的支付通道创建策略
     * @param payWayParam 支付类型
     * @return 支付策略
     */
    public static AbsPayStrategy create(PayWayParam payWayParam) {

        AbsPayStrategy strategy = null;
        PayChannelEnum channelEnum = findByCode(payWayParam.getPayChannel());
        switch (channelEnum) {
            case ALI:
                strategy = SpringUtil.getBean(AliPayStrategy.class);
                break;
            case WECHAT:
                strategy = SpringUtil.getBean(WeChatPayStrategy.class);
                break;
            case UNION_PAY:
                strategy = SpringUtil.getBean(UnionPayStrategy.class);
                break;
            case CASH:
                strategy = SpringUtil.getBean(CashPayStrategy.class);
                break;
            case WALLET:
                strategy = SpringUtil.getBean(WalletPayStrategy.class);
                break;
            case VOUCHER:
                strategy = SpringUtil.getBean(VoucherStrategy.class);
                break;
            case CREDIT_CARD:
                break;
            case APPLE_PAY:
                break;
            default:
                throw new PayUnsupportedMethodException();
        }
        // noinspection ConstantConditions
        strategy.setPayWayParam(payWayParam);
        return strategy;
    }

    /**
     * 根据传入的支付类型批量创建策略, 异步支付在后面
     */
    public static List<AbsPayStrategy> createDesc(List<PayWayParam> payWayParamList) {
        return create(payWayParamList, true);
    }

    /**
     * 根据传入的支付类型批量创建策略, 默认异步支付在前面
     */
    public static List<AbsPayStrategy> create(List<PayWayParam> payWayParamList) {
        return create(payWayParamList, false);
    }

    /**
     * 根据传入的支付类型批量创建策略
     * @param payWayParamList 支付类型
     * @return 支付策略
     */
    private static List<AbsPayStrategy> create(List<PayWayParam> payWayParamList, boolean description) {
        if (CollectionUtil.isEmpty(payWayParamList)) {
            return Collections.emptyList();
        }
        List<AbsPayStrategy> list = new ArrayList<>(payWayParamList.size());

        // 同步支付
        List<PayWayParam> syncPayWayParamList = payWayParamList.stream()
            .filter(Objects::nonNull)
            .filter(payModeParam -> !ASYNC_TYPE.contains(payModeParam.getPayChannel()))
            .collect(Collectors.toList());

        // 异步支付
        List<PayWayParam> asyncPayWayParamList = payWayParamList.stream()
            .filter(Objects::nonNull)
            .filter(payModeParam -> ASYNC_TYPE.contains(payModeParam.getPayChannel()))
            .collect(Collectors.toList());

        List<PayWayParam> sortList = new ArrayList<>(payWayParamList.size());

        // 异步在后面
        if (description) {
            sortList.addAll(syncPayWayParamList);
            sortList.addAll(asyncPayWayParamList);
        }
        else {
            sortList.addAll(asyncPayWayParamList);
            sortList.addAll(syncPayWayParamList);
        }

        // 此处有一个根据Type的反转排序，
        sortList.stream().filter(Objects::nonNull).forEach(payMode -> list.add(create(payMode)));
        return list;
    }

}
