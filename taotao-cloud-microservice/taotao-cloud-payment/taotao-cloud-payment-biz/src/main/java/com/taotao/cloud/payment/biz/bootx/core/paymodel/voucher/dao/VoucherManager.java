package com.taotao.cloud.payment.biz.bootx.core.paymodel.voucher.dao;

import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.base.MpBaseEntity;
import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.payment.core.paymodel.voucher.entity.Voucher;
import cn.bootx.payment.param.paymodel.voucher.VoucherParam;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**   
*
* @author xxm  
* @date 2022/3/14 
*/
@Slf4j
@Repository
@RequiredArgsConstructor
public class VoucherManager extends BaseManager<VoucherMapper, Voucher> {

    /**
     * 分页
     */
    public Page<Voucher> page(PageQuery PageQuery, VoucherParam param){
        Page<Voucher> mpPage = MpUtil.getMpPage(PageQuery, Voucher.class);
        return this.lambdaQuery()
                .ge(Objects.nonNull(param.getStartTime()),Voucher::getStartTime,param.getStartTime())
                .le(Objects.nonNull(param.getEndTime()),Voucher::getEndTime,param.getEndTime())
                .eq(Objects.nonNull(param.getEnduring()),Voucher::getEnduring,param.getEnduring())
                .like(StrUtil.isNotBlank(param.getCardNo()),Voucher::getCardNo,param.getCardNo())
                .like(Objects.nonNull(param.getBatchNo()),Voucher::getBatchNo,param.getBatchNo())
                .orderByDesc(MpBaseEntity::getId)
                .page(mpPage);
    }

    /**
     * 根据卡号查询
     */
    public Optional<Voucher> findByCardNo(String cardNo){
        return this.findByField(Voucher::getCardNo,cardNo);
    }

    /**
     * 根据卡号查询
     */
    public List<Voucher> findByCardNoList(List<String> cardNos){
        return this.findAllByFields(Voucher::getCardNo,cardNos);
    }

    /**
     * 更改状态
     */
    public void changeStatus(Long id, int status){
        this.lambdaUpdate()
                .eq(MpBaseEntity::getId,id)
                .set(Voucher::getStatus,status)
                .update();

    }

    /**
     * 批量更改状态
     */
    public void changeStatusBatch(List<Long> ids, int status){
        this.lambdaUpdate()
                .in(MpBaseEntity::getId,ids)
                .set(Voucher::getStatus,status)
                .update();
    }
}
