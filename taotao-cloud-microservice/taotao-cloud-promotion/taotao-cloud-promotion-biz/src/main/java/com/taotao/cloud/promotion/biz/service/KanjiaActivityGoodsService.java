package com.taotao.cloud.promotion.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.taotao.cloud.promotion.api.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityGoodsOperationDTO;
import com.taotao.cloud.promotion.api.vo.kanjia.KanjiaActivityGoodsListVO;
import com.taotao.cloud.promotion.api.vo.kanjia.KanjiaActivityGoodsParams;
import com.taotao.cloud.promotion.api.vo.kanjia.KanjiaActivityGoodsVO;
import com.taotao.cloud.promotion.biz.entity.KanjiaActivityGoods;
import java.util.List;


/**
 * 砍价业务层
 */
public interface KanjiaActivityGoodsService extends IService<KanjiaActivityGoods> {


    /**
     * 添加砍价活动商品
     *
     * @param kanJiaActivityGoodsDTOS 砍价商品
     * @return 是否添加成功
     */
    Boolean add(KanjiaActivityGoodsOperationDTO kanJiaActivityGoodsDTOS);

    /**
     * 查询砍价活动商品分页信息
     *
     * @param kanJiaActivityGoodsParams 砍价活动商品
     * @param pageVO                    分页信息
     * @return 砍价商品
     */
    IPage<KanjiaActivityGoods> getForPage(KanjiaActivityGoodsParams kanJiaActivityGoodsParams, PageVO pageVO);

    /**
     * 查询砍价活动商品分页信息
     *
     * @param kanJiaActivityGoodsParams 砍价活动商品
     * @param pageVO                    分页信息
     * @return 砍价商品
     */
    IPage<KanjiaActivityGoodsListVO> kanjiaGoodsVOPage(KanjiaActivityGoodsParams kanJiaActivityGoodsParams, PageVO pageVO);

    /**
     * 查询砍价活动商品
     *
     * @param goodsId 砍价活动商品id
     * @return 砍价活动商品信息
     */
    KanjiaActivityGoodsDTO getKanjiaGoodsDetail(String goodsId);

    /**
     * 根据SkuId获取正在进行中的砍价商品
     * @param skuId 商品规格Id
     * @return 砍价商品
     */
    KanjiaActivityGoods getKanjiaGoodsBySkuId(String skuId);

    /**
     * 查询砍价活动商品VO
     * @param id 砍价活动商品ID
     * @return 砍价活动商品
     */
    KanjiaActivityGoodsVO getKanJiaGoodsVO(String id);

    /**
     * 修改看见商品信息
     *
     * @param kanjiaActivityGoodsDTO 砍价商品信息
     * @return 是否修改成功
     */
    boolean updateKanjiaActivityGoods(KanjiaActivityGoodsDTO kanjiaActivityGoodsDTO);

    /**
     * 删除砍价商品
     *
     * @param ids 砍价商品ids
     * @return 是否删除成功
     */
    boolean deleteKanJiaGoods(List<String> ids);

}
