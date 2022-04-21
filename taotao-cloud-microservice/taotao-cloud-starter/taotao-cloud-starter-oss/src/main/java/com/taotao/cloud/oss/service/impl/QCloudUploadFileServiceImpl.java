/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.oss.service.impl;

import com.taotao.cloud.oss.exception.UploadFileException;
import com.taotao.cloud.oss.model.UploadFileInfo;
import com.taotao.cloud.oss.propeties.QCloudProperties;
import com.taotao.cloud.oss.service.AbstractUploadFileService;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

/**
 * NginxUploadFileService
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/08/24 16:12
 */
public class QCloudUploadFileServiceImpl extends AbstractUploadFileService {

	private final QCloudProperties properties;

	public QCloudUploadFileServiceImpl(QCloudProperties properties) {
		this.properties = properties;
	}


	@Override
	protected UploadFileInfo uploadFile(MultipartFile file,
		UploadFileInfo uploadFileInfo) throws UploadFileException {
		return null;
	}

	@Override
	protected UploadFileInfo uploadFile(File file,
		UploadFileInfo uploadFileInfo) throws UploadFileException {
		return null;
	}

	@Override
	public UploadFileInfo delete(UploadFileInfo uploadFileInfo) throws UploadFileException {
		return null;
	}
}

