/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.goods.biz.mapstruct;

import com.taotao.cloud.goods.api.dto.ParametersDTO;
import com.taotao.cloud.goods.api.vo.ParametersVO;
import com.taotao.cloud.goods.biz.entity.Parameters;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * IParametersMapStruct
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:58:27
 */
@Mapper(
	unmappedSourcePolicy = ReportingPolicy.IGNORE,
	unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IParametersMapStruct {

	/**
	 * 实例
	 */
	IParametersMapStruct INSTANCE = Mappers.getMapper(IParametersMapStruct.class);

	/**
	 * 参数参数vos
	 *
	 * @param parameters 参数
	 * @return {@link List }<{@link ParametersVO }>
	 * @since 2022-04-27 16:58:27
	 */
	List<ParametersVO> parametersToParametersVOs(List<Parameters> parameters);

	/**
	 * 参数dtoto参数
	 *
	 * @param parametersDTO 参数dto
	 * @return {@link Parameters }
	 * @since 2022-04-27 16:58:27
	 */
	Parameters parametersDTOToParameters(ParametersDTO parametersDTO);


}
