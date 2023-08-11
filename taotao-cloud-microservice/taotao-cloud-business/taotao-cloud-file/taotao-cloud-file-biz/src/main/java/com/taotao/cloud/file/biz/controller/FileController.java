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

package com.taotao.cloud.file.biz.controller;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.file.api.model.vo.UploadFileVO;
import com.taotao.cloud.file.biz.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件管理管理接口
 *
 * @author Chopper
 * @since 2020/11/26 15:41
 */
@RestController
// @Api(tags = "文件管理接口")
@RequestMapping("/common/common/file")
public class FileController {

//    @Autowired
    private IFileService fileService;

    // @Autowired
    // private Cache cache;
	@Operation(summary = "文件上传", description = "文件上传",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json")),
		responses = {@ApiResponse(description = "是否成功", content = @Content(mediaType = "application/json"))})
	@Parameters({
		@Parameter(name = "type", required = true, description = "类型"),
		@Parameter(name = "file", required = true, description = "文件", schema = @Schema(implementation = MultipartFile.class))
	})
    @PostMapping(value = "/upload")
    public Result<UploadFileVO> upload(
            @RequestParam("type") String type, @NotNull(message = "文件不能为空") @RequestPart("file") MultipartFile file) {
        return Result.success(fileService.uploadFile(type, file));
    }

	@Operation(summary = "给属性分配权限", description = "给属性分配权限，方便权限数据操作",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/x-www-form-urlencoded")),
		responses = {@ApiResponse(description = "已保存数据", content = @Content(mediaType = "application/json"))})
	@Parameters({
		@Parameter(name = "attributeId", required = true, description = "attributeId"),
		@Parameter(name = "permissions[]", required = true, description = "角色对象组成的数组")
	})
	@PutMapping("/xxxxxx")
	public Result<String> assign(@RequestParam(name = "attributeId") String attributeId, @RequestParam(name = "permissions[]") String[] permissions) {
		return Result.success("sdfasdf");
	}

	@Operation(summary = "给用户分配角色", description = "给用户分配角色",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/x-www-form-urlencoded")),
		responses = {@ApiResponse(description = "已分配角色的用户数据", content = @Content(mediaType = "application/json"))})
	@Parameters({
		@Parameter(name = "userId", required = true, description = "userId"),
		@Parameter(name = "roles", required = true, description = "角色对象组成的数组")
	})
	@PutMapping("/sss")
	public Result<String> assign1111(@RequestParam(name = "userId") String userId, @RequestParam(name = "roles") List<String> roles) {
		return Result.success("sdfasdf");
	}

    // @ApiOperation(value = "获取自己的图片资源")
    // @GetMapping
    // @ApiImplicitParam(name = "title", value = "名称模糊匹配")
    // public ResultMessage<IPage<File>> getFileList(@RequestHeader String accessToken, File file,
    //	SearchVO searchVO, PageVO pageVo) {
    //
    //	AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
    //	FileOwnerDTO fileOwnerDTO = new FileOwnerDTO();
    //	//只有买家才写入自己id
    //	if (authUser.getRole().equals(UserEnums.MEMBER)) {
    //		fileOwnerDTO.setOwnerId(authUser.getId());
    //	}//如果是店铺，则写入店铺id
    //	else if (authUser.getRole().equals(UserEnums.STORE)) {
    //		fileOwnerDTO.setOwnerId(authUser.getStoreId());
    //	}
    //	fileOwnerDTO.setUserEnums(authUser.getRole().name());
    //	return ResultUtil.data(fileService.customerPageOwner(fileOwnerDTO, file, searchVO, pageVo));
    // }
    //
    // @ApiOperation(value = "文件重命名")
    // @PostMapping(value = "/rename")
    // public ResultMessage<File> upload(@RequestHeader String accessToken, String id,
    //	String newName) {
    //	AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
    //	File file = fileService.getById(id);
    //	file.setName(newName);
    //	//操作图片属性判定
    //	switch (authUser.getRole()) {
    //		case MEMBER:
    //			if (file.getOwnerId().equals(authUser.getId()) && file.getUserEnums()
    //				.equals(authUser.getRole().name())) {
    //				break;
    //			}
    //			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
    //		case STORE:
    //			if (file.getOwnerId().equals(authUser.getStoreId()) && file.getUserEnums()
    //				.equals(authUser.getRole().name())) {
    //				break;
    //			}
    //			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
    //		case MANAGER:
    //			break;
    //		default:
    //			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
    //	}
    //	fileService.updateById(file);
    //	return ResultUtil.data(file);
    // }
    //
    // @ApiOperation(value = "文件删除")
    // @DeleteMapping(value = "/delete/{ids}")
    // public ResultMessage delete(@RequestHeader String accessToken, @PathVariable List<String>
    // ids) {
    //
    //	AuthUser authUser = UserContext.getAuthUser(cache, accessToken);
    //	fileService.batchDelete(ids, authUser);
    //	return ResultUtil.success();
    // }

}
