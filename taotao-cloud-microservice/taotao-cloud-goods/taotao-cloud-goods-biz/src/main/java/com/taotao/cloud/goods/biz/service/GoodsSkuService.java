package com.taotao.cloud.goods.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.goods.api.dto.GoodsSearchParams;
import com.taotao.cloud.goods.api.dto.GoodsSkuStockDTO;
import com.taotao.cloud.goods.api.vo.GoodsSkuBaseVO;
import com.taotao.cloud.goods.api.vo.GoodsSkuVO;
import com.taotao.cloud.goods.biz.entity.Goods;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import java.util.List;
import java.util.Map;

/**
 * 商品sku业务层
 */
public interface GoodsSkuService extends IService<GoodsSku> {

	/**
	 * 获取商品SKU缓存ID
	 *
	 * @param id SkuId
	 * @return 商品SKU缓存ID
	 */
	static String getCacheKeys(String id) {
		return CachePrefix.GOODS_SKU.getPrefix() + id;
	}

	/**
	 * 获取商品SKU库存缓存ID
	 *
	 * @param id SkuId
	 * @return 商品SKU缓存ID
	 */
	static String getStockCacheKey(String id) {
		return CachePrefix.SKU_STOCK.getPrefix() + id;
	}

	/**
	 * 添加商品sku
	 *
	 * @param skuList sku列表
	 * @param goods   商品信息
	 */
	Boolean add(List<Map<String, Object>> skuList, Goods goods);

	/**
	 * 更新商品sku
	 *
	 * @param skuList            sku列表
	 * @param goods              商品信息
	 * @param regeneratorSkuFlag 是否是否重新生成sku
	 */
	Boolean update(List<Map<String, Object>> skuList, Goods goods, Boolean regeneratorSkuFlag);

	/**
	 * 更新商品sku
	 *
	 * @param goodsSku sku信息
	 */
	Boolean update(GoodsSku goodsSku);

	/**
	 * 清除sku缓存
	 *
	 * @param skuId skuid
	 */
	Boolean clearCache(String skuId);

	/**
	 * 从redis缓存中获取商品SKU信息
	 *
	 * @param id SkuId
	 * @return 商品SKU信息
	 */
	GoodsSku getGoodsSkuByIdFromCache(String id);

	/**
	 * 获取商品sku详情
	 *
	 * @param goodsId 商品ID
	 * @param skuId   skuID
	 * @return 商品sku详情
	 */
	Map<String, Object> getGoodsSkuDetail(String goodsId, String skuId);

	/**
	 * 批量从redis中获取商品SKU信息
	 *
	 * @param ids SkuId集合
	 * @return 商品SKU信息集合
	 */
	List<GoodsSku> getGoodsSkuByIdFromCache(List<String> ids);

	/**
	 * 获取goodsId下所有的goodsSku
	 *
	 * @param goodsId 商品id
	 * @return goodsSku列表
	 */
	List<GoodsSkuVO> getGoodsListByGoodsId(String goodsId);

	/**
	 * 获取goodsId下所有的goodsSku
	 *
	 * @param goodsId 商品id
	 * @return goodsSku列表
	 */
	List<GoodsSku> getGoodsSkuListByGoodsId(String goodsId);

	/**
	 * 根据goodsSku组装goodsSkuVO
	 *
	 * @param list 商品id
	 * @return goodsSku列表
	 */
	List<GoodsSkuVO> getGoodsSkuVOList(List<GoodsSku> list);

	/**
	 * 根据goodsSku组装goodsSkuVO
	 *
	 * @param goodsSku 商品规格
	 * @return goodsSku列表
	 */
	GoodsSkuVO getGoodsSkuVO(GoodsSku goodsSku);

	/**
	 * 分页查询商品sku信息
	 *
	 * @param searchParams 查询参数
	 * @return 商品sku信息
	 */
	PageModel<GoodsSkuBaseVO> getGoodsSkuByPage(GoodsSearchParams searchParams);

	/**
	 * 列表查询商品sku信息
	 *
	 * @param searchParams 查询参数
	 * @return 商品sku信息
	 */
	List<GoodsSku> getGoodsSkuByList(GoodsSearchParams searchParams);

	/**
	 * 更新商品sku状态
	 *
	 * @param goods 商品信息(Id,MarketEnable/AuthFlag)
	 */
	void updateGoodsSkuStatus(Goods goods);

	/**
	 * 发送生成ES商品索引
	 *
	 * @param goods 商品信息
	 */
	Boolean generateEs(Goods goods);

	/**
	 * 更新SKU库存
	 *
	 * @param goodsSkuStockDTOS sku库存修改实体
	 */
	Boolean updateStocks(List<GoodsSkuStockDTO> goodsSkuStockDTOS);

	/**
	 * 更新SKU库存
	 *
	 * @param skuId    SKUId
	 * @param quantity 设置的库存数量
	 */
	Boolean updateStock(String skuId, Integer quantity);

	/**
	 * 获取商品sku库存
	 *
	 * @param skuId 商品skuId
	 * @return 库存数量
	 */
	Integer getStock(String skuId);

	/**
	 * 修改商品库存字段
	 *
	 * @param goodsSkus
	 */
	Boolean updateGoodsStuck(List<GoodsSku> goodsSkus);

	/**
	 * 更新SKU评价数量
	 *
	 * @param skuId SKUId
	 */
	Boolean updateGoodsSkuCommentNum(String skuId);

	/**
	 * 根据商品id获取全部skuId的集合
	 *
	 * @param goodsId goodsId
	 * @return 全部skuId的集合
	 */
	List<String> getSkuIdsByGoodsId(String goodsId);
}
