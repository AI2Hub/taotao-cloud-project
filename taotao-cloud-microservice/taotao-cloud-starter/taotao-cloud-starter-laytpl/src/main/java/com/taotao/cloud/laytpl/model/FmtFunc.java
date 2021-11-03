/*
 * Copyright (c) 2019-2029, Dreamlu (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.laytpl.model;

import com.taotao.cloud.common.utils.DateUtil;
import com.taotao.cloud.laytpl.exception.LayTplException;
import com.taotao.cloud.laytpl.properties.LayTplProperties;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 内置函数
 * <p>
 * 提供给 tpl 和 Thymeleaf 使用
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
public class FmtFunc {

	private final LayTplProperties properties;

	public FmtFunc(LayTplProperties properties) {
		this.properties = properties;
	}

	/**
	 * 对象格式化
	 *
	 * @param object 格式化对象
	 * @return 格式化后的字符串
	 */
	public String format(Object object) {
		if (object instanceof Number) {
			return format(object, properties.getNumPattern());
		} else if (object instanceof Date) {
			return format(object, properties.getDatePattern());
		} else if (object instanceof LocalTime) {
			return format(object, properties.getLocalTimePattern());
		} else if (object instanceof LocalDate) {
			return format(object, properties.getLocalDatePattern());
		} else if (object instanceof LocalDateTime) {
			return format(object, properties.getLocalDateTimePattern());
		}
		throw new LayTplException("未支持的对象格式" + object);
	}

	/**
	 * 对象格式化
	 *
	 * @param object  格式化对象
	 * @param pattern 表达式
	 * @return 格式化后的字符串
	 */
	public String format(Object object, String pattern) {
		if (object instanceof Number) {
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			return decimalFormat.format(object);
		} else if (object instanceof Date) {
			return DateUtil.format((Date) object, pattern);
		} else if (object instanceof TemporalAccessor) {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
			return df.format((TemporalAccessor) object);
		}
		throw new LayTplException("未支持的对象格式" + object);
	}

}
