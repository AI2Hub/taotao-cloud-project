package com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.service;

import cn.bootx.platform.common.core.exception.DataNotExistException;
import cn.bootx.platform.common.core.rest.PageResult;
import cn.bootx.platform.common.core.rest.param.PageParam;
import cn.bootx.platform.common.mybatisplus.util.MpUtil;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.entity.ClientNoticeRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.entity.ClientNoticeTask;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.notice.service.ClientNoticeService;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.dao.ClientNoticeRecordManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.task.notice.dao.ClientNoticeTaskManager;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.notice.ClientNoticeRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.record.notice.ClientNoticeTaskDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.record.ClientNoticeRecordQuery;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.record.ClientNoticeTaskQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 通知任务查询类
 * @author xxm
 * @since 2024/2/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClientNoticeTaskService {

    private final ClientNoticeService clientNoticeService;

    private final ClientNoticeTaskManager taskManager;

    private final ClientNoticeRecordManager recordManager;


    /**
     * 手动触发消息发送
     */
    public void sendData(Long taskId){
    }

    /**
     * 任务分页查询
     */
    public PageResult<ClientNoticeTaskDto> taskPage(PageParam pageParam, ClientNoticeTaskQuery query){
        return MpUtil.convert2DtoPageResult(taskManager.page(pageParam, query));
    }

    /**
     * 任务详情
     */
    public ClientNoticeTaskDto findTaskById(Long id){
        return taskManager.findById(id).map(ClientNoticeTask::toDto).orElseThrow(DataNotExistException::new);
    }

    /**
     * 记录分页
     */
    public PageResult<ClientNoticeRecordDto> recordPage(PageParam pageParam, ClientNoticeRecordQuery query){
        return MpUtil.convert2DtoPageResult(recordManager.page(pageParam, query));
    }

    /**
     * 记录详情
     */
    public ClientNoticeRecordDto findRecordById(Long id){
        return recordManager.findById(id).map(ClientNoticeRecord::toDto).orElseThrow(DataNotExistException::new);
    }
}
