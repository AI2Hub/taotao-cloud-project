/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.taotao.cloud.goods.application.command.dept;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptInsertCmd;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 新增部门执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptInsertCmdExe {

	private final DeptGateway deptGateway;

	private final DeptMapper deptMapper;

	private final DeptConvertor deptConvertor;

	/**
	 * 执行新增部门.
	 * @param cmd 新增部门参数
	 * @return 执行新增结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(DeptInsertCmd cmd) {
		DeptCO co = cmd.getDeptCO();
		long count = deptMapper.selectCount(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getName, co.getName()));
		if (count > 0) {
			throw new SystemException("部门已存在，请重新填写");
		}
		return Result.of(deptGateway.insert(deptConvertor.toEntity(co)));
	}

}
