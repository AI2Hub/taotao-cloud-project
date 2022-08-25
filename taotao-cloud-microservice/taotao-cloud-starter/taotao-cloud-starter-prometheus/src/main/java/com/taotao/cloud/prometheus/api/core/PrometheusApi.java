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

package com.taotao.cloud.prometheus.api.core;

import com.taotao.cloud.prometheus.api.pojo.AlertMessage;
import com.taotao.cloud.prometheus.api.pojo.TargetGroup;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * prometheus http sd
 *
 * @author L.cm
 */
@RestController
@RequestMapping("actuator/prometheus")
public class PrometheusApi {
	private final String activeProfile;
	private final DiscoveryClient discoveryClient;
	private final ApplicationEventPublisher eventPublisher;

	public PrometheusApi(String activeProfile, DiscoveryClient discoveryClient, ApplicationEventPublisher eventPublisher) {
		this.activeProfile = activeProfile;
		this.discoveryClient = discoveryClient;
		this.eventPublisher = eventPublisher;
	}

	@GetMapping("sd")
	public List<TargetGroup> getList() {
		List<String> serviceIdList = discoveryClient.getServices();
		if (serviceIdList == null || serviceIdList.isEmpty()) {
			return Collections.emptyList();
		}
		List<TargetGroup> targetGroupList = new ArrayList<>();
		for (String serviceId : serviceIdList) {
			List<ServiceInstance> instanceList = discoveryClient.getInstances(serviceId);
			List<String> targets = new ArrayList<>();
			for (ServiceInstance instance : instanceList) {
				targets.add(String.format("%s:%d", instance.getHost(), instance.getPort()));
			}
			Map<String, String> labels = new HashMap<>(4);
			// 1. 环境
			if (StringUtils.hasText(activeProfile)) {
				labels.put("profile", activeProfile);
			}
			// 2. 服务名
			labels.put("__meta_prometheus_job", serviceId);
			targetGroupList.add(new TargetGroup(targets, labels));
		}
		return targetGroupList;
	}

	@PostMapping("alerts")
	public ResponseEntity<Object> postAlerts(@RequestBody AlertMessage message) {
		eventPublisher.publishEvent(message);
		return ResponseEntity.ok().build();
	}

}
