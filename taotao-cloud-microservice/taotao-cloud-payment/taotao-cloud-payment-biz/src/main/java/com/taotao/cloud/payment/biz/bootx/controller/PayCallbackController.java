package com.taotao.cloud.payment.biz.bootx.controller;

import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.alipay.service.AliPayCallbackService;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.service.WeChatPayCallbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
*
* @author xxm
* @date 2021/2/27
*/
@Slf4j
@Tag(name ="支付回调")
@RestController
@RequestMapping("/pay/callback")
@AllArgsConstructor
public class PayCallbackController {
    private final AliPayCallbackService aliPayCallbackService;
    private final WeChatPayCallbackService weChatPayCallbackService;

    @SneakyThrows
    @Operation(summary = "支付宝回调")
    @PostMapping("/aliPay")
    public String aliPay(HttpServletRequest request){
        Map<String, String> stringStringMap = AliPayApi.toMap(request);
        return aliPayCallbackService.payCallback(stringStringMap);
    }

    @SneakyThrows
    @Operation(summary = "微信支付回调")
    @PostMapping("/wechat")
    public String wechat(HttpServletRequest request){
        String xmlMsg = HttpKit.readData(request);
        Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
        return weChatPayCallbackService.payCallback(params);
    }
}
