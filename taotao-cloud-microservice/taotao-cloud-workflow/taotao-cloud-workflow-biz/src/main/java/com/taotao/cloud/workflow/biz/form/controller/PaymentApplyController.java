package com.taotao.cloud.workflow.biz.form.controller;

import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorEntity;
import com.taotao.cloud.workflow.biz.engine.enums.FlowStatusEnum;
import com.taotao.cloud.workflow.biz.engine.service.FlowTaskOperatorService;
import com.taotao.cloud.workflow.biz.form.entity.PaymentApplyEntity;
import com.taotao.cloud.workflow.biz.form.model.paymentapply.PaymentApplyForm;
import com.taotao.cloud.workflow.biz.form.model.paymentapply.PaymentApplyInfoVO;
import com.taotao.cloud.workflow.biz.form.service.PaymentApplyService;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 付款申请单
 */
@Tag(tags = "付款申请单", value = "PaymentApply")
@RestController
@RequestMapping("/api/workflow/Form/PaymentApply")
public class PaymentApplyController {

    @Autowired
    private PaymentApplyService paymentApplyService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取付款申请单信息
     *
     * @param id 主键值
     * @return
     */
    @Operation("获取付款申请单信息")
    @GetMapping("/{id}")
    public Result<PaymentApplyInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        PaymentApplyInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), PaymentApplyInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            PaymentApplyEntity entity = paymentApplyService.getInfo(id);
            vo = JsonUtil.getJsonToBean(entity, PaymentApplyInfoVO.class);
        }
        return Result.success(vo);
    }

    /**
     * 新建付款申请单
     *
     * @param paymentApplyForm 表单对象
     * @return
     */
    @Operation("新建付款申请单")
    @PostMapping
    public Result create(@RequestBody PaymentApplyForm paymentApplyForm) throws WorkFlowException {
        PaymentApplyEntity entity = JsonUtil.getJsonToBean(paymentApplyForm, PaymentApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(paymentApplyForm.getStatus())) {
            paymentApplyService.save(entity.getId(), entity);
            return Result.success(MsgCode.SU002.get());
        }
        paymentApplyService.submit(entity.getId(), entity,paymentApplyForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }

    /**
     * 修改付款申请单
     *
     * @param paymentApplyForm 表单对象
     * @param id               主键
     * @return
     */
    @Operation("修改付款申请单")
    @PutMapping("/{id}")
    public Result update(@RequestBody PaymentApplyForm paymentApplyForm, @PathVariable("id") String id) throws WorkFlowException {
        PaymentApplyEntity entity = JsonUtil.getJsonToBean(paymentApplyForm, PaymentApplyEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(paymentApplyForm.getStatus())) {
            paymentApplyService.save(id, entity);
            return Result.success(MsgCode.SU002.get());
        }
        paymentApplyService.submit(id, entity,paymentApplyForm.getCandidateList());
        return Result.success(MsgCode.SU006.get());
    }
}
