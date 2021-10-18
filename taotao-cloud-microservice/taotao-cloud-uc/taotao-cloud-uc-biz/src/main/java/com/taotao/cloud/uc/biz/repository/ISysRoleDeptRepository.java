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
package com.taotao.cloud.uc.biz.repository;

import com.taotao.cloud.uc.biz.entity.SysRoleDept;
import com.taotao.cloud.web.base.repository.BaseSuperRepository;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SysRoleRepository
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:47:05
 */
@Repository
public interface ISysRoleDeptRepository extends JpaRepository<SysRoleDept, Long> {

}
