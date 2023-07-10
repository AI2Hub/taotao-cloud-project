package com.taotao.cloud.payment.biz.daxpay.core.channel.voucher.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 储值卡扣款记录列表
 * @author xxm
 * @since 2023/6/29
 */
@Data
@Accessors(chain = true)
public class VoucherRecord {

    /** 卡号 */
    private String cardNo;

    /** 扣款金额 */
    private BigDecimal amount;
}
