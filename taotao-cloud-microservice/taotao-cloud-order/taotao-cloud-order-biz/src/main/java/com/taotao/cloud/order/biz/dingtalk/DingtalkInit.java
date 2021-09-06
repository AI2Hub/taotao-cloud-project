/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.order.biz.dingtalk;

import com.taotao.cloud.dingtalk.entity.DingerRequest;
import com.taotao.cloud.dingtalk.enums.MessageSubType;
import com.taotao.cloud.dingtalk.model.DingerRobot;
import com.taotao.cloud.dingtalk.model.DingerSender;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * DingtalkInit
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/09/03 15:43
 */
@Component
public class DingtalkInit implements InitializingBean {

	@Autowired
	private DingerRobot dingerSender;

	@Override
	public void afterPropertiesSet() throws Exception {
		dingerSender.send(
			MessageSubType.TEXT,
			DingerRequest.request("taotao cloud 测试机器人")
		);
	}
}
