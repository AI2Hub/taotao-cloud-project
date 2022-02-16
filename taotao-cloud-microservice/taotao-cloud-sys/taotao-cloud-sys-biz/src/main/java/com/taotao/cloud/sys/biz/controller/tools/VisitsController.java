package com.taotao.cloud.sys.biz.controller.tools;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.RequestUtil;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.biz.service.IVisitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * VisitsController
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-11 16:26:45
 */
@Validated
@RestController
@Tag(name = "工具管理-访问记录管理API", description = "工具管理-访问记录管理API")
@RequestMapping("/sys/tools/visits")
public class VisitsController {

	private final IVisitsService IVisitsService;

	public VisitsController(IVisitsService IVisitsService) {
		this.IVisitsService = IVisitsService;
	}

	@Operation(summary = "创建访问记录", description = "创建访问记录", method = CommonConstant.POST)
	@RequestLogger(description = "创建访问记录")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping
	public Result<Boolean> create() {
		IVisitsService.count(RequestUtil.getHttpServletRequest());
		return Result.success(true);
	}

	@Operation(summary = "查询访问记录", description = "查询访问记录", method = CommonConstant.GET)
	@RequestLogger(description = "查询访问记录")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping
	public Result<Object> get() {
		return Result.success(IVisitsService.get());
	}

	@Operation(summary = "查询图表数据", description = "查询图表数据", method = CommonConstant.GET)
	@RequestLogger(description = "查询图表数据")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/chartData")
	public Result<Object> getChartData() {
		return Result.success(IVisitsService.getChartData());
	}
}
