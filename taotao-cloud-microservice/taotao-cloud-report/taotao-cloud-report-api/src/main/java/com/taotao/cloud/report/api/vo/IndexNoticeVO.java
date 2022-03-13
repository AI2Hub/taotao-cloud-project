package com.taotao.cloud.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息提示
 *
 * @author Bulbasaur
 * @since 2020/12/9 14:25
 */
@Data
public class IndexNoticeVO {

    @Schema(description =  "待处理商品审核")
    private Long goods;

    @Schema(description =  "待处理店铺入驻审核")
    private Long store;

    @Schema(description =  "待处理售后申请")
    private Long refund;

    @Schema(description =  "待处理投诉审核")
    private Long complain;

    @Schema(description =  "待处理分销员提现申请")
    private Long distributionCash;

    @Schema(description =  "待处理商家结算")
    private Long waitPayBill;

}
