package com.taotao.cloud.payment.biz.daxpay.controller;

import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.dto.KeyValue;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.channel.alipay.service.AlipayConfigService;
import cn.bootx.platform.daxpay.dto.paymodel.alipay.AlipayConfigDto;
import cn.bootx.platform.daxpay.param.paymodel.alipay.AlipayConfigParam;
import cn.bootx.platform.daxpay.param.paymodel.alipay.AlipayConfigQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xxm
 * @date 2021/2/26
 */
@Tag(name = "支付宝配置")
@RestController
@RequestMapping("/alipay")
@AllArgsConstructor
public class AlipayConfigController {

    private final AlipayConfigService alipayConfigService;

    @Operation(summary = "添加")
    @PostMapping("/add")
    public ResResult<Void> add(@RequestBody AlipayConfigParam param) {
        alipayConfigService.add(param);
        return Res.ok();
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody AlipayConfigParam param) {
        alipayConfigService.update(param);
        return Res.ok();
    }

    @Operation(summary = "启用指定的支付宝配置")
    @PostMapping("/setUpActivity")
    public ResResult<Void> setUpActivity(Long id) {
        alipayConfigService.setUpActivity(id);
        return Res.ok();
    }

    @Operation(summary = "清除指定的支付宝配置")
    @PostMapping("/clearActivity")
    public ResResult<Void> clearActivity(Long id) {
        alipayConfigService.clearActivity(id);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<AlipayConfigDto>> page(PageParam pageParam, AlipayConfigQuery param) {
        return Res.ok(alipayConfigService.page(pageParam, param));
    }

    @Operation(summary = "根据Id查询")
    @GetMapping("/findById")
    public ResResult<AlipayConfigDto> findById(Long id) {
        return Res.ok(alipayConfigService.findById(id));
    }

    @Operation(summary = "支付宝支持支付方式")
    @GetMapping("/findPayWayList")
    public ResResult<List<KeyValue>> findPayWayList() {
        return Res.ok(alipayConfigService.findPayWayList());
    }

}
