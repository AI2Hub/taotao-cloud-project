package com.taotao.cloud.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分类统计VO
 *
 * @author Bulbasaur
 * @since 2020/12/10 15:42
 */
@Data
public class CategoryStatisticsDataVO {


    @ApiModelProperty("一级分类ID")
    private String categoryId;

    @ApiModelProperty("一级分类名称")
    private String categoryName;

    @Schema(description =  "销售数量")
    private String num;

    @Schema(description =  "销售金额")
    private Double price;
}
