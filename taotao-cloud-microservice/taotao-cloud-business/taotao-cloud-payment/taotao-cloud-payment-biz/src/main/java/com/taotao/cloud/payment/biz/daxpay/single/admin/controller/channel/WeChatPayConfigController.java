package com.taotao.cloud.payment.biz.daxpay.single.admin.controller.channel;

import cn.hutool.core.codec.Base64;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.service.WeChatPayConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author xxm
 * @since 2021/3/19
 */
@Tag(name = "微信支付配置")
@RestController
@RequestMapping("/wechat/pay/config")
@AllArgsConstructor
public class WeChatPayConfigController {

    private final WeChatPayConfigService weChatPayConfigService;

    @Operation(summary = "获取配置")
    @GetMapping("/getConfig")
    public ResResult<WeChatPayConfigDto> getConfig() {
        return Res.ok(weChatPayConfigService.getConfig().toDto());
    }

    @Operation(summary = "更新")
    @PostMapping("/update")
    public ResResult<Void> update(@RequestBody WeChatPayConfigParam param) {
        weChatPayConfigService.update(param);
        return Res.ok();
    }
    @Operation(summary = "微信支持支付方式")
    @GetMapping("/findPayWays")
    public ResResult<List<LabelValue>> findPayWays() {
        return Res.ok(weChatPayConfigService.findPayWays());
    }

    @SneakyThrows
    @Operation(summary = "将文件转换成base64")
    @PostMapping("/toBase64")
    public ResResult<String> toBase64(MultipartFile file){
        return Res.ok(Base64.encode(file.getBytes()));
    }
}
