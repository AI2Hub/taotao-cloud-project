package com.taotao.cloud.payment.biz.bootx.core.refund.service;

import cn.bootx.common.core.exception.DataNotExistException;
import cn.bootx.common.core.rest.PageResult;
import cn.bootx.common.core.rest.param.PageQuery;
import cn.bootx.common.mybatisplus.util.MpUtil;
import cn.bootx.payment.core.refund.dao.RefundRecordManager;
import cn.bootx.payment.core.refund.entity.RefundRecord;
import cn.bootx.payment.dto.refund.RefundRecordDto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**   
* 退款
* @author xxm  
* @date 2022/3/2 
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class RefundRecordService {
    private final RefundRecordManager refundRecordManager;

    /**
     * 分页查询
     */
    public PageResult<RefundRecordDto> page(PageQuery PageQuery,RefundRecordDto param){
        Page<RefundRecord> page = refundRecordManager.page(PageQuery,param);
        return MpUtil.convert2DtoPageResult(page);
    }


    /**
     * 根据id查询
     */
    public RefundRecordDto findById(Long id){
        return refundRecordManager.findById(id)
                .map(RefundRecord::toDto)
                .orElseThrow(DataNotExistException::new);
    }
}
