/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.goods.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.goods.api.model.dto.BrandDTO;
import com.taotao.cloud.goods.api.model.page.BrandPageQuery;
import com.taotao.cloud.goods.api.model.vo.BrandVO;
import com.taotao.cloud.goods.biz.model.entity.Brand;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Map;

/**
 * 商品品牌业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:59:15
 */
public interface IBrandService extends BaseSuperService<Brand, Long> {

	/**
	 * 根据条件分页获取品牌列表
	 *
	 * @param page 条件参数
	 * @return 品牌列表
	 */
	IPage<Brand> getBrandsByPage(BrandPageDTO page);

	/**
	 * 删除品牌
	 *
	 * @param ids 品牌id
	 */
	void deleteBrands(List<String> ids);

	/**
	 * 根据分类ID获取品牌列表
	 *
	 * @param categoryId 分类ID
	 * @return 品牌列表
	 */
	List<Brand> getBrandsByCategory(String categoryId);

	/**
	 * 根据分类ID获取品牌列表
	 *
	 * @param categoryIds 分类ID
	 * @return 品牌列表
	 */
	List<Map<String, Object>> getBrandsMapsByCategory(List<String> categoryIds, String columns);

	/**
	 * 添加品牌
	 *
	 * @param brandVO 品牌信息
	 * @return 添加结果
	 */
	boolean addBrand(BrandVO brandVO);

	/**
	 * 更新品牌
	 *
	 * @param brandVO 品牌信息
	 * @return 更新结果
	 */
	boolean updateBrand(BrandVO brandVO);

	/**
	 * 更新品牌是否可用
	 *
	 * @param brandId 品牌ID
	 * @param disable 是否不可用
	 * @return 更新结果
	 */
	boolean brandDisable(String brandId, boolean disable);
}
