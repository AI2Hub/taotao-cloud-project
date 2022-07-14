package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.Valid;
import jnpf.base.ActionResult;
import jnpf.base.util.RegexUtils;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.LetterServiceEntity;
import jnpf.form.model.letterservice.LetterServiceForm;
import jnpf.form.model.letterservice.LetterServiceInfoVO;
import jnpf.form.service.LetterServiceService;
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
 * 发文单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
@Api(tags = "发文单", value = "LetterService")
@RestController
@RequestMapping("/api/workflow/Form/LetterService")
public class LetterServiceController {

    @Autowired
    private LetterServiceService letterServiceService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取发文单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取发文单信息")
    @GetMapping("/{id}")
    public ActionResult<LetterServiceInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        LetterServiceInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), LetterServiceInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            LetterServiceEntity entity = letterServiceService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, LetterServiceInfoVO.class);
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建发文单
     *
     * @param letterServiceForm 表单对象
     * @return
     */
    @ApiOperation("新建发文单")
    @PostMapping
    public ActionResult create(@RequestBody @Valid LetterServiceForm letterServiceForm) throws WorkFlowException {
        if (letterServiceForm.getShareNum() != null && StringUtil.isNotEmpty(letterServiceForm.getShareNum()) && !RegexUtils.checkDigit2(letterServiceForm.getShareNum())) {
            return ActionResult.fail("份数只能输入正整数");
        }
        LetterServiceEntity entity = JsonUtil.getJsonToBean(letterServiceForm, LetterServiceEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(letterServiceForm.getStatus())) {
            letterServiceService.save(entity.getId(), entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        letterServiceService.submit(entity.getId(), entity,letterServiceForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改发文单
     *
     * @param letterServiceForm 表单对象
     * @param id                主键
     * @return
     */
    @ApiOperation("修改发文单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody @Valid LetterServiceForm letterServiceForm, @PathVariable("id") String id) throws WorkFlowException {
        if (letterServiceForm.getShareNum() != null && StringUtil.isNotEmpty(letterServiceForm.getShareNum()) && !RegexUtils.checkDigit2(letterServiceForm.getShareNum())) {
            return ActionResult.fail("份数只能输入正整数");
        }
        LetterServiceEntity entity = JsonUtil.getJsonToBean(letterServiceForm, LetterServiceEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(letterServiceForm.getStatus())) {
            letterServiceService.save(id, entity);
            return ActionResult.success(MsgCode.SU002.get());
        }
        letterServiceService.submit(id, entity,letterServiceForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
