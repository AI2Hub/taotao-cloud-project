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

package com.taotao.cloud.goods.application.command.dept.query;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptGetQry;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查看部门执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptGetQryExe {

	private final DeptGateway deptGateway;

	private final DeptConvertor deptConvertor;

	/**
	 * 执行查看部门.
	 * @param qry 查看部门参数
	 * @return 部门
	 */
	@DS(TENANT)
	public Result<DeptCO> execute(DeptGetQry qry) {
		return Result.of(deptConvertor.convertClientObject(deptGateway.getById(qry.getId())));
	}

}
