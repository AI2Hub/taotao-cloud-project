package com.taotao.cloud.workflow.biz.form.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import jnpf.base.ActionResult;
import jnpf.constant.MsgCode;
import jnpf.engine.entity.FlowTaskOperatorEntity;
import jnpf.engine.enums.FlowStatusEnum;
import jnpf.engine.service.FlowTaskOperatorService;
import jnpf.exception.DataException;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.WarehouseEntryEntity;
import jnpf.form.entity.WarehouseReceiptEntity;
import jnpf.form.model.warehousereceipt.WarehouseReceiptEntityInfoModel;
import jnpf.form.model.warehousereceipt.WarehouseReceiptForm;
import jnpf.form.model.warehousereceipt.WarehouseReceiptInfoVO;
import jnpf.form.service.WarehouseReceiptService;
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
 * 入库申请单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Api(tags = "入库申请单", value = "WarehouseReceipt")
@RestController
@RequestMapping("/api/workflow/Form/WarehouseReceipt")
public class WarehouseReceiptController {

    @Autowired
    private WarehouseReceiptService warehouseReceiptService;
    @Autowired
    private FlowTaskOperatorService flowTaskOperatorService;

    /**
     * 获取入库申请单信息
     *
     * @param id 主键值
     * @return
     */
    @ApiOperation("获取入库申请单信息")
    @GetMapping("/{id}")
    public ActionResult<WarehouseReceiptInfoVO> info(@PathVariable("id") String id, String taskOperatorId) throws DataException {
        WarehouseReceiptInfoVO vo = null;
        boolean isData = true;
        if (StringUtil.isNotEmpty(taskOperatorId)) {
            FlowTaskOperatorEntity operator = flowTaskOperatorService.getInfo(taskOperatorId);
            if (operator != null) {
                if (StringUtil.isNotEmpty(operator.getDraftData())) {
                    vo = JsonUtil.getJsonToBean(operator.getDraftData(), WarehouseReceiptInfoVO.class);
                    isData = false;
                }
            }
        }
        if (isData) {
            WarehouseReceiptEntity entity = warehouseReceiptService.getInfo(id);
            List<WarehouseEntryEntity> entityList = warehouseReceiptService.getWarehouseEntryList(id);
            vo = JsonUtil.getJsonToBean(entity, WarehouseReceiptInfoVO.class);
            vo.setEntryList(JsonUtil.getJsonToList(entityList, WarehouseReceiptEntityInfoModel.class));
        }
        return ActionResult.success(vo);
    }

    /**
     * 新建入库申请单
     *
     * @param warehouseReceiptForm 表单对象
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("新建入库申请单")
    @PostMapping
    public ActionResult create(@RequestBody WarehouseReceiptForm warehouseReceiptForm) throws WorkFlowException {
        WarehouseReceiptEntity warehouse = JsonUtil.getJsonToBean(warehouseReceiptForm, WarehouseReceiptEntity.class);
        List<WarehouseEntryEntity> warehouseEntryList = JsonUtil.getJsonToList(warehouseReceiptForm.getEntryList(), WarehouseEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(warehouseReceiptForm.getStatus())) {
            warehouseReceiptService.save(warehouse.getId(), warehouse, warehouseEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        warehouseReceiptService.submit(warehouse.getId(), warehouse, warehouseEntryList,warehouseReceiptForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }

    /**
     * 修改入库申请单
     *
     * @param warehouseReceiptForm 表单对象
     * @param id                   主键
     * @return
     * @throws WorkFlowException
     */
    @ApiOperation("修改入库申请单")
    @PutMapping("/{id}")
    public ActionResult update(@RequestBody WarehouseReceiptForm warehouseReceiptForm, @PathVariable("id") String id) throws WorkFlowException {
        WarehouseReceiptEntity warehouse = JsonUtil.getJsonToBean(warehouseReceiptForm, WarehouseReceiptEntity.class);
        List<WarehouseEntryEntity> warehouseEntryList = JsonUtil.getJsonToList(warehouseReceiptForm.getEntryList(), WarehouseEntryEntity.class);
        if (FlowStatusEnum.save.getMessage().equals(warehouseReceiptForm.getStatus())) {
            warehouseReceiptService.save(id, warehouse, warehouseEntryList);
            return ActionResult.success(MsgCode.SU002.get());
        }
        warehouseReceiptService.submit(id, warehouse, warehouseEntryList,warehouseReceiptForm.getCandidateList());
        return ActionResult.success(MsgCode.SU006.get());
    }
}
