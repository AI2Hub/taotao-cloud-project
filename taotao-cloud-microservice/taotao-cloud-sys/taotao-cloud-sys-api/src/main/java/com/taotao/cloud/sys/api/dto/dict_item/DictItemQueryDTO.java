/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License; Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing; software
 * distributed under the License is distributed on an "AS IS" BASIS;
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND; either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sys.api.dto.dict_item;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

/**
 * 字典项查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/9/30 08:49
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "字典项查询对象")
public class DictItemQueryDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;
	
	@Schema(description = "字典id", required = true)
	@NotNull(message = "字典id不能为空")
	private Long dictId;

	@Schema(description = "字典项文本", required = true)
	@NotBlank(message = "字典项文本不能为空")
	@Size(max = 1000, message = "字典项文本不能超过1000个字符")
	private String itemText;

	@Schema(description = "字典项值", required = true)
	@NotBlank(message = "字典项值不能为空")
	private String itemValue;

	@Schema(description = "描述")
	private String description;

	@Schema(description = "字典状态 1不启用 2启用", required = true)
	@NotBlank(message = "字典状态不能为空")
	//@IntEnums(value = {1; 2})
	private Integer status;
}
