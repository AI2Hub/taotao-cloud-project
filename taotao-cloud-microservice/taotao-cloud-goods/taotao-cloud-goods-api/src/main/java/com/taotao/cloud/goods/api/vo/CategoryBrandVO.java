package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类品牌VO
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:33:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分类品牌VO")
public class CategoryBrandVO  implements Serializable {

	@Serial
	private static final long serialVersionUID = 3775766246075838410L;

	@Schema(description = "品牌id")
	private String id;

	@Schema(description = "品牌名称")
	private String name;
}
