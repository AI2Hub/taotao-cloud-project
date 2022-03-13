package com.taotao.cloud.promotion.api.dto;

import cn.lili.modules.goods.entity.dos.GoodsSku;
import cn.lili.modules.promotion.entity.dos.PromotionGoods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * 促销商品数据传输对象
 *
 * @author paulG
 * @since 2020/10/9
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PromotionGoodsDTO extends PromotionGoods {

    private static final long serialVersionUID = 9206970681612883421L;

    @Schema(description =  "商品id")
    private String goodsId;

    @Schema(description =  "商品名称")
    private String goodsName;

    @Schema(description =  "商品图片")
    private String goodsImage;

    public PromotionGoodsDTO(GoodsSku sku) {
        super(sku);
    }
}
