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
package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.core.configuration.OkhttpAutoConfiguration.OkHttpService;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.sys.api.dubbo.IDubboRegionService;
import com.taotao.cloud.sys.biz.mapper.IRegionMapper;
import com.taotao.cloud.sys.biz.model.entity.region.Region;
import com.taotao.cloud.sys.biz.repository.cls.RegionRepository;
import com.taotao.cloud.sys.biz.repository.inf.IRegionRepository;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * RegionServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:37:52
 */
@Service
@DubboService(interfaceClass = IDubboRegionService.class, validation = "true")
public class DubboRegionServiceImpl extends
	BaseSuperServiceImpl<IRegionMapper, Region, RegionRepository, IRegionRepository, Long>
	implements IDubboRegionService {

	@Autowired
	private OkHttpService okHttpService;
	@Autowired
	private RedisRepository redisRepository;

}
