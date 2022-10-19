/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.gateway.shenyu;

import com.alibaba.nacos.client.config.impl.LocalConfigInfoProcessor;
import java.io.File;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Shenyu bootstrap.
 */
@SpringBootApplication
public class TaoTaoCloudGatewayShenyuApplication {

	/**
	 * Main Entrance.
	 *
	 * @param args startup arguments
	 */
	public static void main(final String[] args) {
		setNacosProperty();
		SpringApplication.run(TaoTaoCloudGatewayShenyuApplication.class, args);
	}

	public static void setNacosProperty() {
		/**
		 * 设置nacos客户端日志和快照目录
		 *
		 * @see LocalConfigInfoProcessor
		 */
		String userHome = System.getProperty("user.home");
		System.setProperty("JM.LOG.PATH", userHome + File.separator + "logs" + File.separator
			+ "taotao-cloud-gateway-springcloud");
		System.setProperty("JM.SNAPSHOT.PATH", userHome + File.separator + "logs" + File.separator
			+ "taotao-cloud-gateway-springcloud");
		System.setProperty("nacos.logging.default.config.enabled", "true");
	}
}
