package com.taotao.cloud.payment.biz.daxpay.single.admin.controller.channel;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wallet.service.WalletConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 钱包配置
 * @author xxm
 * @since 2024/2/17
 */
@Tag(name = "钱包配置")
@RestController
@RequestMapping("/wallet/config")
@RequiredArgsConstructor
public class WalletConfigController {
    private final WalletConfigService service;


    @Operation(summary = "获取配置")
    @GetMapping("/getConfig")
    public ResResult<WalletConfigDto> getConfig() {
        return Res.ok(service.getConfig().toDto());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WalletConfigParam param) {
        service.update(param);
        return Res.ok();
    }

    @Operation(summary = "支付宝支持支付方式")
    @GetMapping("/findPayWays")
    public ResResult<List<LabelValue>> findPayWays() {
        return Res.ok(service.findPayWays());
    }
}
