package com.taotao.cloud.payment.biz.bootx.controller;

import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service.WalletLogService;
import com.taotao.cloud.payment.biz.bootx.dto.paymodel.wallet.WalletLogDto;
import com.taotao.cloud.payment.biz.bootx.param.paymodel.wallet.WalletLogQueryParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 钱包日志相关接口
 * @author xxm
 * @date 2020/12/8
 */
@Tag(name ="钱包日志相关的接口")
@RestController
@RequestMapping("/wallet/log")
@AllArgsConstructor
public class WalletLogController {
    private final WalletLogService walletLogService;


    @Operation(summary = "个人钱包日志")
    @PostMapping("/pageByPersonal")
    public ResResult<PageResult<WalletLogDto>> pageByPersonal(@ParameterObject PageParam pageParam,@ParameterObject WalletLogQueryParam param){
        return Res.ok(walletLogService.pageByPersonal(pageParam,param));
    }
    
    @Operation(summary = "查询钱包日志(分页)")
    @GetMapping("/page")
    public ResResult<PageResult<WalletLogDto>> page(@ParameterObject PageParam pageParam,@ParameterObject WalletLogQueryParam param) {
        return Res.ok(walletLogService.page(pageParam,param));
    }

    @Operation(summary = "根据钱包id查询钱包日志(分页)")
    @GetMapping("/pageByWalletId")
    public ResResult<PageResult<WalletLogDto>> pageByWalletId(@ParameterObject PageParam pageParam,@ParameterObject WalletLogQueryParam param) {
        return Res.ok(walletLogService.pageByWalletId(pageParam,param));
    }

}
