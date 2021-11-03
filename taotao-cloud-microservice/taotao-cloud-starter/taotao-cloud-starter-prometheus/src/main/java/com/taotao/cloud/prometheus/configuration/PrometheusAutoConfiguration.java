/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
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

package com.taotao.cloud.prometheus.configuration;

import com.taotao.cloud.prometheus.api.PrometheusApi;
import com.taotao.cloud.prometheus.api.ReactivePrometheusApi;
import com.taotao.cloud.prometheus.properties.PrometheusProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ConditionalOnReactiveDiscoveryEnabled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * prometheus http sd
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = PrometheusProperties.PREFIX, name = "enabled", havingValue = "true")
public class PrometheusAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBean(DiscoveryClient.class)
	@ConditionalOnDiscoveryEnabled
	// @ConditionalOnBlockingDiscoveryEnabled
	@ConditionalOnProperty(value = "spring.cloud.discovery.blocking.enabled")
	public static class PrometheusApiConfiguration {

		@Bean
		public PrometheusApi prometheusApi(DiscoveryClient discoveryClient,
			ApplicationEventPublisher eventPublisher) {
			return new PrometheusApi(discoveryClient, eventPublisher);
		}

	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnBean(ReactiveDiscoveryClient.class)
	@ConditionalOnDiscoveryEnabled
	@ConditionalOnReactiveDiscoveryEnabled
	public static class ReactivePrometheusApiConfiguration {

		@Bean
		public ReactivePrometheusApi reactivePrometheusApi(ReactiveDiscoveryClient discoveryClient,
			ApplicationEventPublisher eventPublisher) {
			return new ReactivePrometheusApi(discoveryClient, eventPublisher);
		}

	}

}
