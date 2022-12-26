package com.taotao.cloud.workflow.biz.form.model.applymeeting;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 会议申请
 *
 */
@Data
public class ApplyMeetingForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "查看人")
    private String lookPeople;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "会议名称不能为空")
    @Schema(description = "会议名称")
    private String conferenceName;
    @NotNull(message = "结束日期不能为空")
    @Schema(description = "结束日期")
    private Long endDate;
    @Schema(description = "出席人")
    private String attendees;
    @Schema(description = "纪要员")
    private String memo;
    @NotBlank(message = "申请人员不能为空")
    @Schema(description = "申请人员")
    private String applyUser;
    @Schema(description = "会议类型")
    private String conferenceType;
    @Schema(description = "会议室")
    private String conferenceRoom;
    @Schema(description = "会议主题")
    private String conferenceTheme;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "管理人")
    private String administrator;
    @Schema(description = "其他出席人")
    private String otherAttendee;
    @Schema(description = "预计人数")
    private String estimatePeople;
    @Schema(description = "申请材料")
    private String applyMaterial;
    @NotBlank(message = "所属职务不能为空")
    @Schema(description = "所属职务")
    private String position;
    @Schema(description = "会议描述")
    private String describe;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "预计金额")
    private BigDecimal estimatedAmount;
    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期")
    private Long startDate;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;

}
