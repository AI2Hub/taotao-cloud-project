package com.taotao.cloud.payment.biz.bootx.controller;

import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.payment.biz.bootx.core.notify.service.PayNotifyRecordService;
import com.taotao.cloud.payment.biz.bootx.dto.notify.PayNotifyRecordDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* 回调记录
* @author xxm
* @date 2021/7/22
*/
@Tag(name ="支付回调记录")
@RestController
@RequestMapping("/pay/notify/record")
@RequiredArgsConstructor
public class PayNotifyRecordController {
    private final PayNotifyRecordService notifyRecordService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public ResResult<PageResult<PayNotifyRecordDto>> page(PageParam pageParam,PayNotifyRecordDto param){
        return Res.ok(notifyRecordService.page(pageParam,param));
    }

    @Operation(summary = "根据id查询")
    @GetMapping("/findById")
    public ResResult<PayNotifyRecordDto> findById(Long id){
        return Res.ok(notifyRecordService.findById(id));
    }
}
