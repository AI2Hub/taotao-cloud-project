package com.taotao.cloud.workflow.biz.form.model.incomerecognition;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * 收入确认分析表
 */
@Data
public class IncomeRecognitionForm {

	@NotNull(message = "必填")
	@Schema(description = "紧急程度")
	private Integer flowUrgent;
	@NotBlank(message = "必填")
	@Schema(description = "联系人姓名")
	private String contactName;
	@NotBlank(message = "必填")
	@Schema(description = "联系电话")
	private String contacPhone;
	@Schema(description = "到款金额")
	private BigDecimal actualAmount;
	@NotBlank(message = "必填")
	@Schema(description = "到款银行")
	private String moneyBank;
	@Schema(description = "备注")
	private String description;
	@NotBlank(message = "必填")
	@Schema(description = "客户名称")
	private String customerName;
	@Schema(description = "合同总金额")
	private BigDecimal totalAmount;
	@NotBlank(message = "必填")
	@Schema(description = "流程标题")
	private String flowTitle;
	@Schema(description = "未付金额")
	private BigDecimal unpaidAmount;
	@Schema(description = "已付金额")
	private BigDecimal amountPaid;
	@NotBlank(message = "必填")
	@Schema(description = "结算月份")
	private String settlementMonth;
	@NotBlank(message = "必填")
	@Schema(description = "合同编码")
	private String contractNum;
	@NotNull(message = "必填")
	@Schema(description = "到款日期")
	private Long paymentDate;
	@NotBlank(message = "必填")
	@Schema(description = "流程主键")
	private String flowId;
	@NotBlank(message = "必填")
	@Schema(description = "流程单据")
	private String billNo;
	@Schema(description = "联系QQ")
	private String contactQQ;
	@Schema(description = "提交/保存 0-1")
	private String status;
	@Schema(description = "候选人")
	private Map<String, List<String>> candidateList;

}
