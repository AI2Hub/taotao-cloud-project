package com.taotao.cloud.payment.biz.bootx.controller;

import cn.bootx.platform.common.core.annotation.IgnoreAuth;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.daxpay.core.cashier.service.CashierService;
import cn.bootx.daxpay.dto.pay.PayResult;
import cn.bootx.daxpay.exception.payment.PayUnsupportedMethodException;
import cn.bootx.daxpay.param.cashier.CashierCombinationPayParam;
import cn.bootx.daxpay.param.cashier.CashierSinglePayParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * @author xxm
 * @date 2022/2/23
 */
@IgnoreAuth
@Tag(name = "结算台")
@RestController
@RequestMapping("/cashier")
@RequiredArgsConstructor
public class CashierController {

    private final CashierService cashierService;

    @Operation(summary = "发起支付(单渠道,包括聚合付款码方式)")
    @PostMapping("/singlePay")
    public ResResult<PayResult> singlePay(@RequestBody CashierSinglePayParam cashierSinglePayParam) {
        return Res.ok(cashierService.singlePay(cashierSinglePayParam));
    }

    @Operation(summary = "发起支付(组合支付)")
    @PostMapping("/combinationPay")
    public ResResult<PayResult> combinationPay(@RequestBody CashierCombinationPayParam param) {
        return Res.ok(cashierService.combinationPay(param));
    }

    @Operation(summary = "扫码聚合支付(单渠道)")
    @GetMapping("/aggregatePay")
    public ModelAndView aggregatePay(String key, @RequestHeader(USER_AGENT) String ua) {
        try {
            String url = cashierService.aggregatePay(key, ua);
            return new ModelAndView("redirect:" + url);
        }
        catch (PayUnsupportedMethodException e) {
            return new ModelAndView("errorCashier");
        }
    }

    @Operation(summary = "微信jsapi支付(回调)")
    @GetMapping("/wxJsapiPay")
    public ModelAndView wxJsapiPay(String code, String state) {
        Map<String, String> map = cashierService.wxJsapiPay(code, state);
        // 跳转页面, 调起微信jsapi支付
        return new ModelAndView("wechatJsapiPay").addAllObjects(map);
    }

}
