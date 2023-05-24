package com.taotao.cloud.payment.biz.daxpay.core.cashier.service;

import cn.bootx.platform.daxpay.code.pay.PayChannelCode;
import cn.bootx.platform.daxpay.code.pay.PayWayExtraCode;
import cn.bootx.platform.daxpay.code.pay.PayStatusCode;
import cn.bootx.platform.daxpay.code.pay.PayWayCode;
import cn.bootx.platform.daxpay.code.paymodel.WeChatPayCode;
import cn.bootx.platform.daxpay.core.aggregate.entity.AggregatePayInfo;
import cn.bootx.platform.daxpay.core.aggregate.service.AggregateService;
import cn.bootx.platform.daxpay.core.pay.service.PayService;
import cn.bootx.platform.daxpay.core.channel.wechat.dao.WeChatPayConfigManager;
import cn.bootx.platform.daxpay.core.channel.wechat.entity.WeChatPayConfig;
import cn.bootx.platform.daxpay.dto.pay.PayResult;
import cn.bootx.platform.daxpay.exception.payment.PayFailureException;
import cn.bootx.platform.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.platform.daxpay.param.cashier.CashierCombinationPayParam;
import cn.bootx.platform.daxpay.param.cashier.CashierSinglePayParam;
import cn.bootx.platform.daxpay.param.pay.PayModeParam;
import cn.bootx.platform.daxpay.param.pay.PayParam;
import cn.bootx.platform.daxpay.util.PayModelUtil;
import cn.bootx.platform.baseapi.core.parameter.dao.SystemParamManager;
import cn.bootx.platform.baseapi.core.parameter.entity.SystemParameter;
import cn.bootx.platform.common.core.util.BigDecimalUtil;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.hutool.core.collection.CollUtil;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.kit.WxPayKit;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 结算台
 *
 * @author xxm
 * @date 2022/2/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CashierService {

    private final PayService payService;

    private final AggregateService aggregateService;

    private final WeChatPayConfigManager weChatPayConfigManager;

    private final SystemParamManager systemParamManager;

    /**
     * 发起支付(单渠道支付)
     */
    public PayResult singlePay(CashierSinglePayParam param) {

        // 如果是聚合支付,存在付款码时特殊处理(聚合扫码支付不用额外处理)
        if (Objects.equals(PayChannelCode.AGGREGATION, param.getPayChannel())) {
            int payChannel = aggregateService.getPayChannel(param.getAuthCode());
            param.setPayChannel(payChannel);
        }
        // 构建支付方式参数
        PayModeParam payModeParam = new PayModeParam().setPayChannel(param.getPayChannel())
            .setPayWay(param.getPayWay())
            .setAmount(param.getAmount());

        // 处理附加参数
        HashMap<String, String> map = new HashMap<>(1);
        map.put(PayWayExtraCode.AUTH_CODE, param.getAuthCode());
        map.put(PayWayExtraCode.OPEN_ID, param.getOpenId());
        map.put(PayWayExtraCode.VOUCHER_NO, param.getVoucherNo());
        String extraParamsJson = PayModelUtil.buildExtraParamsJson(param.getPayChannel(), map);
        payModeParam.setExtraParamsJson(extraParamsJson);

        PayParam payParam = new PayParam().setTitle(param.getTitle())
            .setBusinessId(param.getBusinessId())
            .setUserId(SecurityUtil.getUserIdOrDefaultId())
            .setPayModeList(Collections.singletonList(payModeParam));
        PayResult payResult = payService.pay(payParam);

        if (PayStatusCode.TRADE_REFUNDED == payResult.getPayStatus()) {
            throw new PayFailureException("已经退款");
        }
        return payResult;
    }

    /**
     * 扫码发起自动支付
     */
    public String aggregatePay(String key, String ua) {
        CashierSinglePayParam cashierSinglePayParam = new CashierSinglePayParam().setPayWay(PayWayCode.QRCODE);
        // 判断是哪种支付方式
        if (ua.contains(PayChannelCode.UA_ALI_PAY)) {
            cashierSinglePayParam.setPayChannel(PayChannelCode.ALI);
        }
        else if (ua.contains(PayChannelCode.UA_WECHAT_PAY)) {
            // 跳转微信授权页面, 调用jsapi进行支付
            return this.wxJsapiAuth(key);
        }
        else {
            throw new PayUnsupportedMethodException();
        }

        AggregatePayInfo aggregatePayInfo = aggregateService.getAggregateInfo(key);
        cashierSinglePayParam.setTitle(aggregatePayInfo.getTitle())
            .setAmount(aggregatePayInfo.getAmount())
            .setBusinessId(aggregatePayInfo.getBusinessId());
        PayResult payResult = this.singlePay(cashierSinglePayParam);
        return payResult.getAsyncPayInfo().getPayBody();
    }

    /**
     * 微信jsapi支付 - 跳转到授权页面
     */
    private String wxJsapiAuth(String key) {
        WeChatPayConfig config = weChatPayConfigManager.findActivity()
            .orElseThrow(() -> new PayFailureException("未找到启用的微信支付配置"));
        WxMpService wxMpService = getWxMpService(config.getAppId(), config.getAppSecret());
        // 回调地址为 结算台微信jsapi支付的回调地址
        SystemParameter systemParameter = systemParamManager.findByParamKey(WeChatPayCode.JSAPI_REDIRECT_URL)
            .orElseThrow(() -> new PayFailureException("微信支付回调地址参数不存在"));
        String url = systemParameter.getValue() + "cashier/wxJsapiPay";
        return wxMpService.getOAuth2Service().buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, key);
    }

    /**
     * 微信jsapi支付 - 回调发起预支付, 同时调起微信页面jsapi支付
     * @param code 微信授权码, 用来获取id
     * @param state 聚合支付参数记录的key
     * @return 页面中调起jsapi支付的参数
     */
    @SneakyThrows
    public Map<String, String> wxJsapiPay(String code, String state) {
        WeChatPayConfig config = weChatPayConfigManager.findActivity()
            .orElseThrow(() -> new PayFailureException("未找到启用的微信支付配置"));
        WxMpService wxMpService = this.getWxMpService(config.getAppId(), config.getAppSecret());
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        String openId = accessToken.getOpenId();
        AggregatePayInfo aggregatePayInfo = aggregateService.getAggregateInfo(state);
        // 构造微信API支付参数
        CashierSinglePayParam cashierSinglePayParam = new CashierSinglePayParam().setPayChannel(PayChannelCode.WECHAT)
            .setPayWay(PayWayCode.JSAPI)
            .setTitle(aggregatePayInfo.getTitle())
            .setAmount(aggregatePayInfo.getAmount())
            .setOpenId(openId)
            .setBusinessId(aggregatePayInfo.getBusinessId());
        PayResult payResult = this.singlePay(cashierSinglePayParam);

        return WxPayKit.prepayIdCreateSign(payResult.getAsyncPayInfo().getPayBody(), config.getAppId(),
                config.getApiKeyV2(), SignType.HMACSHA256);
    }

    /**
     * 获取微信公众号API的Service
     */
    private WxMpService getWxMpService(String appId, String secret) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(secret); // 设置微信公众号的app corpSecret
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    /**
     * 组合支付
     */
    public PayResult combinationPay(CashierCombinationPayParam param) {
        // 处理支付参数
        List<PayModeParam> payModeList = param.getPayModeList();
        // 删除小于等于零的
        payModeList.removeIf(payModeParam -> BigDecimalUtil.compareTo(payModeParam.getAmount(), BigDecimal.ZERO) < 1);
        if (CollUtil.isEmpty(payModeList)) {
            throw new PayFailureException("支付参数有误");
        }
        // 发起支付
        PayParam payParam = new PayParam().setTitle(param.getTitle())
            .setBusinessId(param.getBusinessId())
            .setUserId(SecurityUtil.getUserIdOrDefaultId())
            .setPayModeList(param.getPayModeList());
        PayResult payResult = payService.pay(payParam);

        if (PayStatusCode.TRADE_REFUNDED == payResult.getPayStatus()) {
            throw new PayFailureException("已经退款");
        }
        return payResult;
    }

}
