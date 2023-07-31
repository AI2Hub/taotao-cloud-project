package com.taotao.cloud.payment.biz.daxpay.controller;

import cn.bootx.platform.common.core.annotation.OperateLog;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.Res;
import cn.bootx.platform.common.core.rest.ResResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletQueryService;
import cn.bootx.platform.daxpay.core.channel.wallet.service.WalletService;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletDto;
import cn.bootx.platform.daxpay.dto.channel.wallet.WalletInfoDto;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletQueryParam;
import cn.bootx.platform.daxpay.param.channel.wallet.WalletRechargeParam;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.bootx.platform.iam.param.user.UserInfoParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 钱包
 *
 * @author xxm
 * @since 2021/2/24
 */
@Tag(name = "钱包相关的接口")
@RestController
@RequestMapping("/wallet/admin")
@AllArgsConstructor
public class WalletAdminController {

    private final WalletService walletService;

    private final WalletQueryService walletQueryService;

    @Operation(summary = "开通用户钱包操作")
    @PostMapping("/createWallet")
    public ResResult<Void> createWallet(Long userId, String mchCode, String mchAppCode) {
        walletService.createWallet(userId,mchCode,mchAppCode);
        return Res.ok();
    }

    @Operation(summary = "批量开通用户钱包操作")
    @PostMapping("/createWalletBatch")
    public ResResult<Void> createWalletBatch(@RequestBody List<Long> userIds, String mchCode, String mchAppCode) {
        walletService.createWalletBatch(userIds,mchCode,mchAppCode);
        return Res.ok();
    }

    @Operation(summary = "解锁钱包")
    @OperateLog(title = "解锁钱包", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/unlock")
    public ResResult<Void> unlock(Long walletId) {
        walletService.unlock(walletId);
        return Res.ok();
    }

    @Operation(summary = "锁定钱包")
    @OperateLog(title = "锁定钱包", businessType = OperateLog.BusinessType.UPDATE, saveParam = true)
    @PostMapping("/lock")
    public ResResult<Void> lock(Long walletId) {
        walletService.lock(walletId);
        return Res.ok();
    }

    @Operation(summary = "充值操作(增减余额)")
    @PostMapping("/changerBalance")
    public ResResult<Void> changerBalance(@RequestBody WalletRechargeParam param) {
        walletService.changerBalance(param);
        return Res.ok();
    }

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<WalletDto>> page(PageParam pageParam, WalletQueryParam param) {
        return Res.ok(walletQueryService.page(pageParam, param));
    }

    @Operation(summary = "分页(未开通钱包的用户)")
    @GetMapping("/pageByNotWallet")
    public ResResult<PageResult<UserInfoDto>> pageByNotWallet(PageParam pageParam,String mchCode, UserInfoParam param) {
        return Res.ok(walletQueryService.pageByNotWallet(pageParam,mchCode,param));
    }

    @Operation(summary = "根据钱包ID查询钱包")
    @GetMapping("/findById")
    public ResResult<WalletDto> findById(Long walletId) {
        return Res.ok(walletQueryService.findById(walletId));
    }

    @Operation(summary = "获取钱包综合信息")
    @GetMapping("/getWalletInfo")
    public ResResult<WalletInfoDto> getWalletInfo(Long walletId) {
        return Res.ok(walletQueryService.getWalletInfo(walletId));
    }

}
