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
package com.taotao.cloud.demo.rocketmq.comsumer.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.SubscribableChannel;

/**
 *
 */
@EnableBinding({RocketMqConfig.MySink.class})
public class RocketMqConfig {

	public interface MySink {

		@Input(Sink.INPUT)
		SubscribableChannel input();

		@Input("input2")
		SubscribableChannel input2();

		@Input("input3")
		SubscribableChannel input3();
	}
}
