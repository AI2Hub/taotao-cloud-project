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

package com.taotao.cloud.goods.application.command.dict;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.dto.dict.DictGetQry;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查看字典执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DictGetQryExe {

	private final DictGateway dictGateway;

	private final DictConvertor dictConvertor;

	/**
	 * 执行查看字典.
	 * @param qry 查看字典参数
	 * @return 字典
	 */
	@DS(TENANT)
	public Result<DictCO> execute(DictGetQry qry) {
		return Result.of(dictConvertor.convertClientObject(dictGateway.getById(qry.getId())));
	}

}
