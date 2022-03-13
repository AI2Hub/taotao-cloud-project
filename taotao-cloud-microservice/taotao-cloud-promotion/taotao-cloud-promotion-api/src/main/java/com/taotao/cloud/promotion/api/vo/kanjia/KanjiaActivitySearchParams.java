package com.taotao.cloud.promotion.api.vo.kanjia;

import cn.hutool.core.util.StrUtil;
import cn.lili.modules.promotion.entity.vos.BasePromotionsSearchParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砍价活动搜索参数
 *
 * 
 * @date: 2021/7/13 2:41 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KanjiaActivitySearchParams extends BasePromotionsSearchParams {

    @Schema(description =  "砍价活动ID")
    private String id;

    @Schema(description =  "砍价商品SkuID")
    private String kanjiaActivityGoodsId;

    @Schema(description =  "会员ID" ,hidden = true)
    private String memberId;

    @Schema(description =  "状态")
    private String status;

    @Schema(description =  "邀请活动ID，有值说明是被邀请人")
    private String kanjiaActivityId;

    @Schema(description =  "规格商品ID" ,hidden = true)
    private String goodsSkuId;


    public <T> QueryWrapper<T> wrapper() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(StrUtil.isNotEmpty(kanjiaActivityId), "id", kanjiaActivityId);
        queryWrapper.eq(StrUtil.isNotEmpty(kanjiaActivityGoodsId), "kanjia_activity_goods_id", kanjiaActivityGoodsId);
        queryWrapper.eq(StrUtil.isNotEmpty(goodsSkuId), "sku_id", goodsSkuId);
        queryWrapper.eq(StrUtil.isNotEmpty(memberId), "member_id", memberId);
        queryWrapper.eq(StrUtil.isNotEmpty(status), "status", status);
        return queryWrapper;
    }
}

