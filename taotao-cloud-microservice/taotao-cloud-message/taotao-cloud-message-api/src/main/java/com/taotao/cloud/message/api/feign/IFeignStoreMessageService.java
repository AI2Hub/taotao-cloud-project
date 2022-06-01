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
package com.taotao.cloud.message.api.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.message.api.feign.fallback.FeignNoticeMessageServiceFallback;
import com.taotao.cloud.message.api.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.api.vo.StoreMessageVO;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "remoteWithdrawService", value = ServiceName.TAOTAO_CLOUD_AFTERSALE_CENTER, fallbackFactory = FeignNoticeMessageServiceFallback.class)
public interface IFeignStoreMessageService {


	IPage<StoreMessageVO> getPage(StoreMessageQueryVO storeMessageQueryVO, PageParam pageParam);
}

