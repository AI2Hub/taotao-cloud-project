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

package com.taotao.cloud.workflow.biz.app.model;

import jnpf.util.treeutil.SumTree;
import lombok.Data;

/**
 * @author 
 * @version V3.1.0
 * @copyright 
 */
@Data
public class AppTreeModel extends SumTree {
    private String enCode;
    private Long num;
    private String fullName;
    private String formType;
    private String type;
    private String icon;
    private String category;
    private String iconBackground;
    private String visibleType;
    private String creatorUser;
    private Long creatorTime;
    private Long sortCode;
    private Integer enabledMark;
    private Boolean isData;
}
