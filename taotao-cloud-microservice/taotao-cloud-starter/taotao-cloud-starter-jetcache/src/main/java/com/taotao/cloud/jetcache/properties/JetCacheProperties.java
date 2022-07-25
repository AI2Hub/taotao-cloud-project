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

package com.taotao.cloud.jetcache.properties;

import com.google.common.base.MoreObjects;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * jetcache metrics 配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-25 08:53:27
 */
@RefreshScope
@ConfigurationProperties(JetCacheProperties.PREFIX)
public class JetCacheProperties {

	public static final String PREFIX = "taotao.cloud.jetcache";

	/**
	 * 开启 jetcache metrics，默认：true
	 */
	private boolean enabled = true;

	/**
	 * 分布式缓存Redis端是否进行数据脱敏， 默认值，true
	 * <p>
	 * Hibernate二级缓存中，会基于SQL进行数据缓存。这种缓存以SQL作为key，一方面这个Key会比较长，另一方面SQL明文存入Redis缺少安全性。
	 * 通过这个配置，可以设定是否对Hibernate二级缓存的SQL进行脱敏，脱敏后会将SQL转换为MD5值。当然这也会带来一定的性能损耗
	 */
	private Boolean desensitization = true;

	/**
	 * 退出时是否清理远端缓存，默认值，false
	 * <p>
	 * 服务退出时，会清理本地以及远端的缓存，为了在集群情况下避免因此导致的缓存击穿问题，默认退出时不清除远端缓存。
	 */
	private Boolean clearRemoteOnExit = false;

	/**
	 * 是否允许存储空值
	 */
	private Boolean allowNullValues = true;

	/**
	 * 缓存名称转换分割符。默认值，"-"
	 * <p>
	 * 默认缓存名称采用 Redis Key 格式（使用 ":" 分割），使用 ":" 分割的字符串作为Map的Key，":"会丢失。 指定一个分隔符，用于 ":" 分割符的转换
	 */
	private String separator = "-";

	/**
	 * 针对不同实体单独设置的过期时间，如果不设置，则统一使用默认时间。
	 */
	private Map<String, Expire> expires = new HashMap<>();

	public Boolean getDesensitization() {
		return desensitization;
	}

	public void setDesensitization(Boolean desensitization) {
		this.desensitization = desensitization;
	}

	public Boolean getClearRemoteOnExit() {
		return clearRemoteOnExit;
	}

	public void setClearRemoteOnExit(Boolean clearRemoteOnExit) {
		this.clearRemoteOnExit = clearRemoteOnExit;
	}

	public Boolean getAllowNullValues() {
		return allowNullValues;
	}

	public void setAllowNullValues(Boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	public Map<String, Expire> getExpires() {
		return expires;
	}

	public void setExpires(Map<String, Expire> expires) {
		this.expires = expires;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("desensitization", desensitization)
			.add("clearRemoteOnExit", clearRemoteOnExit)
			.add("allowNullValues", allowNullValues)
			.add("separator", separator)
			.toString();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
