package com.taotao.cloud.payment.biz.bootx.dto.paymodel.voucher;

import com.taotao.cloud.payment.biz.bootx.dto.payment.BasePaymentDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
*
* @author xxm
* @date 2022/3/14
*/
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "储值卡支付记录")
public class VoucherPaymentDto extends BasePaymentDto {
}
