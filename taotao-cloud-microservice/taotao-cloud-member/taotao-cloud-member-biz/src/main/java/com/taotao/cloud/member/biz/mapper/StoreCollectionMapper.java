package com.taotao.cloud.member.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.member.api.vo.StoreCollectionVO;
import com.taotao.cloud.member.biz.entity.StoreCollection;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 会员收藏数据处理层
 *
 * 
 * @since 2020-02-25 14:10:16
 */
public interface StoreCollectionMapper extends BaseMapper<StoreCollection> {

    /**
     * 会员店铺收藏分页
     *
     * @param page         分页
     * @param queryWrapper 查询条件
     * @return
     */
    @Select("select s.id,s.store_name,s.store_logo,s.self_operated  from li_store s INNER JOIN li_store_collection sc ON s.id=sc.store_id  ${ew.customSqlSegment} ")
    IPage<StoreCollectionVO> storeCollectionVOList(IPage<StoreCollectionVO> page, @Param(Constants.WRAPPER) Wrapper<StoreCollectionVO> queryWrapper);
}
