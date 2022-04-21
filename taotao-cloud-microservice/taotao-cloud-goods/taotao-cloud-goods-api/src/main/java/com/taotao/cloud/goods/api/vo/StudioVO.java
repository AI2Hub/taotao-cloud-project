package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import lombok.experimental.SuperBuilder;

/**
 * 直播间VO
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-15 20:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class StudioVO extends StudioBaseVO {

	@Schema(description = "直播间商品列表")
	private List<CommodityBaseVO> commodityList;

}
