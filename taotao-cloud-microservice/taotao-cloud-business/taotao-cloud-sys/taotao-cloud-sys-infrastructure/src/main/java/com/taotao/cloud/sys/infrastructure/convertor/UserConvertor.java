/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.taotao.cloud.member.infratructure.convertor;

import org.laokou.auth.domain.user.User;
import org.laokou.auth.dto.user.clientobject.UserCO;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.i18n.dto.Convertor;
import org.mapstruct.Mapper;

import static org.laokou.common.i18n.common.SysConstants.SPRING;

/**
 * 用户转换器.
 *
 * @author laokou
 */
@Mapper(componentModel = SPRING)
public interface UserConvertor extends Convertor<UserCO, User, UserDO> {

}
