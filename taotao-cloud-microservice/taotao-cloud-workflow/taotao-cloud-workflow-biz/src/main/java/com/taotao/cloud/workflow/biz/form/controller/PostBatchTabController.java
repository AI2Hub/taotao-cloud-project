package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.PostBatchTabEntity;
import jnpf.form.model.postbatchtab.PostBatchTabForm;
import jnpf.form.model.postbatchtab.PostBatchTabInfoVO;
import jnpf.form.service.PostBatchTabService;
import jnpf.util.JsonUtil;
import jnpf.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发文呈批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "发文呈批表", value = "PostBatchTab")
@RestController
@RequestMapping("/api/workflow/Form/PostBatchTab")
public class PostBatchTabController {

    @Autowired
    private PostBatchTabService postBatchTabService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取发文呈批表信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取发文呈批表信息")
    @GetMapping("/{id}")
    public ActionResult<PostBatchTabInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        PostBatchTabInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), PostBatchTabInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            PostBatchTabEntity entity = postBatchTabService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, PostBatchTabInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建发文呈批表
     *
     * @param postBatchTabForm 表单对象
     * @return
     */
    @ApiOperation("新建发文呈批表")
    @PostMapping
    public ActionResult create(@RequestBody PostBatchTabForm postBatchTabForm) throws WorkFlowException {
        PostBatchTabEntity entity = JsonUtil.getJsonToBean(postBatchTabForm, PostBatchTabEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(postBatchTabForm.getStatus())) {
            postBatchTabService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        postBatchTabService.submit(entity.getId(), entity,postBatchTabForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改发文呈批表
     *
     * @param postBatchTabForm 表单对象
     * @param id               主键
     * @return
     */
    @ApiOperation("修改发文呈批表")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody PostBatchTabForm postBatchTabForm, @PathVariable("id") String id) throws WorkFlowException {
        PostBatchTabEntity entity = JsonUtil.getJsonToBean(postBatchTabForm, PostBatchTabEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(postBatchTabForm.getStatus())) {
            postBatchTabService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        postBatchTabService.submit(id, entity,postBatchTabForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
