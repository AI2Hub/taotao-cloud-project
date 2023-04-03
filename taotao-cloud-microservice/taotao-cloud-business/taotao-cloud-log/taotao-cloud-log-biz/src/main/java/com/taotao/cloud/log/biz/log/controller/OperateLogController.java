package com.taotao.cloud.log.biz.log.controller;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.biz.log.dto.OperateLogDto;
import com.taotao.cloud.log.biz.log.param.OperateLogParam;
import com.taotao.cloud.log.biz.log.service.OperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志
 *
 * @author shuigedeng
 * @date 2021/9/8
 */
@Tag(name = "操作日志")
@RestController
@RequestMapping("/log/operate")
@RequiredArgsConstructor
public class OperateLogController {
	private final OperateLogService operateLogService;

	@Operation(summary = "分页")
	@GetMapping("/page")
	public Result<PageResult<OperateLogDto>> page(OperateLogParam operateLogParam) {
		return Result.success(operateLogService.page(operateLogParam));
	}

	@Operation(summary = "获取")
	@GetMapping("/findById")
	public Result<OperateLogDto> findById(Long id) {
		return Result.success(operateLogService.findById(id));
	}
}
