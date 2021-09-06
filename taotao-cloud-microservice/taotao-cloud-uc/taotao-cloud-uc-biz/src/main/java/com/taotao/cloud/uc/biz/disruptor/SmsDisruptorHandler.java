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
package com.taotao.cloud.uc.biz.disruptor;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.disruptor.annotation.EventRule;
import com.taotao.cloud.disruptor.event.DisruptorBindEvent;
import com.taotao.cloud.disruptor.handler.DisruptorHandler;
import com.taotao.cloud.disruptor.handler.HandlerChain;
import org.springframework.stereotype.Component;

/**
 * EmailDisruptorHandler
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/09/03 18:02
 */
@EventRule("/Event-Output/TagA-Output/**")
@Component("smsHandler")
public class SmsDisruptorHandler implements DisruptorHandler<DisruptorBindEvent> {

	@Override
	public void doHandler(DisruptorBindEvent event,
		HandlerChain<DisruptorBindEvent> handlerChain) throws Exception {
		LogUtil.info(event.toString());
	}
}
