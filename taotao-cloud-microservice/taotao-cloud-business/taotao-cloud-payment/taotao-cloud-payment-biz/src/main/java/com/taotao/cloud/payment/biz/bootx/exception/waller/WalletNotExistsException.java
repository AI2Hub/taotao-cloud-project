package com.taotao.cloud.payment.biz.bootx.exception.waller;

import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.daxpay.code.PaymentCenterErrorCode;

/**
 * 钱包不存在
 *
 * @author xxm
 * @date 2020/12/8
 */
public class WalletNotExistsException extends BizException {

    public WalletNotExistsException() {
        super(PaymentCenterErrorCode.WALLET_NOT_EXISTS, "钱包不存在");
    }

}
