package com.taotao.cloud.payment.biz.bootx.core.paymodel.wallet.service;

import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageParam;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.payment.core.paymodel.wallet.dao.WalletLogManager;
import cn.bootx.payment.dto.paymodel.wallet.WalletLogDto;
import cn.bootx.payment.param.paymodel.wallet.WalletLogQueryParam;
import cn.bootx.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
* 钱包日志
* @author xxm
* @date 2020/12/8
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class WalletLogService {
    private final WalletLogManager walletLogManager;


    /**
     * 个人钱包日志分页
     */
    public PageResult<WalletLogDto> pageByPersonal(PageParam pageParam, WalletLogQueryParam param){
        Long userId = SecurityUtil.getUserId();
        return MpUtil.convert2DtoPageResult(walletLogManager.pageByUserId(pageParam,param,userId));
    }

    /**
     * 钱包日志分页
     */
    public PageResult<WalletLogDto> page(PageParam pageParam, WalletLogQueryParam param){
        return MpUtil.convert2DtoPageResult(walletLogManager.page(pageParam,param));
    }

    /**
     * 根据钱包id查询钱包日志(分页)
     */
    public PageResult<WalletLogDto> pageByWalletId(PageParam pageParam, WalletLogQueryParam param) {
        return MpUtil.convert2DtoPageResult(walletLogManager.pageByWalletId(pageParam,param));
    }
}
