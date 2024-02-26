package cn.bootx.platform.daxpay.exception.waller;

import cn.bootx.platform.common.core.exception.FatalException;
import com.taotao.cloud.payment.biz.daxpay.single.core.code.DaxPayErrorCode;

/**
 * 钱包日志错误
 *
 * @author xxm
 * @since 2020/12/8
 */
public class WalletLogError extends FatalException {

    public WalletLogError() {
        super(DaxPayErrorCode.WALLET_LOG_ERROR, "钱包日志错误");
    }

}
