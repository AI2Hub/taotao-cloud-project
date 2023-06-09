/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.authentication.stamp;

import com.taotao.cloud.auth.biz.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.captcha.support.core.definition.AbstractRenderer;
import com.taotao.cloud.captcha.support.core.definition.domain.Metadata;
import com.taotao.cloud.captcha.support.core.dto.Captcha;
import com.taotao.cloud.captcha.support.core.dto.Verification;
import com.taotao.cloud.security.springsecurity.core.constants.OAuth2Constants;
import org.dromara.hutool.core.data.id.IdUtil;
import org.springframework.beans.factory.InitializingBean;

import java.time.Duration;

/**
 * <p>Description: 锁定账户签章管理 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/8 21:27
 */
public class LockedUserDetailsStampManager extends AbstractRenderer implements InitializingBean {
	private final OAuth2AuthenticationProperties authenticationProperties;

	public LockedUserDetailsStampManager(RedisRepository redisRepository, OAuth2AuthenticationProperties authenticationProperties) {
		super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL);
		this.authenticationProperties = authenticationProperties;
	}

	public LockedUserDetailsStampManager(RedisRepository redisRepository, Duration expire, OAuth2AuthenticationProperties authenticationProperties) {
		super(redisRepository, OAuth2Constants.CACHE_NAME_TOKEN_LOCKED_USER_DETAIL, expire);
		this.authenticationProperties = authenticationProperties;
	}

	@Override
	public String nextStamp(String key) {
		return IdUtil.fastSimpleUUID();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.setExpire(authenticationProperties.getSignInFailureLimited().getExpire());
	}


	@Override
	public Metadata draw() {
		return null;
	}

	@Override
	public Captcha getCapcha(String key) {
		return null;
	}

	@Override
	public boolean verify(Verification verification) {
		return false;
	}

	@Override
	public String getCategory() {
		return null;
	}
}
