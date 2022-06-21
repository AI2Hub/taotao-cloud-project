package com.taotao.cloud.goods.api.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * StoreGoodsLabelVO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:31:53
 */
@RecordBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreGoodsLabelDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "店铺商品分类名称")
	private String labelName;

	@Schema(description = "层级, 从0开始")
	private Integer level;

	@Schema(description = "店铺商品分类排序")
	private Integer sortOrder;

	@Schema(description = "父id, 根节点为0")
	private Long parentId;

	@Schema(description = "店铺ID")
	private Long storeId;
}
