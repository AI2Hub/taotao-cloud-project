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

package com.taotao.cloud.sys.application.command.dict.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "OptionCO", description = "下拉框选择参数项参数")
public class OptionCO  {

	@Serial
	private static final long serialVersionUID = -4146348495335527374L;

	@Schema(name = "label", description = "标签")
	private String label;

	@Schema(name = "value", description = "值")
	private String value;

}
