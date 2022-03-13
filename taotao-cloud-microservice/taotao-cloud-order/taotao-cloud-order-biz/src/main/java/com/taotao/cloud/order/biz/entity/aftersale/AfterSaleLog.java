package com.taotao.cloud.order.biz.entity.aftersale;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 售后日志
 */
@Entity
@Table(name = AfterSaleLog.TABLE_NAME)
@TableName(AfterSaleLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = AfterSaleLog.TABLE_NAME, comment = "售后日志")
public class AfterSaleLog extends BaseSuperEntity<AfterSaleLog, Long> {

	public static final String TABLE_NAME = "li_after_sale_log";

	/**
	 * 应用ID
	 */
	@Schema(description =  "售后服务单号")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String sn;
	/**
	 * 应用ID
	 */
	@Schema(description =  "操作者id(可以是卖家)")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String operatorId;

	/**
	 * @see UserEnums
	 */
	@Schema(description =  "操作者类型")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String operatorType;

	/**
	 * 应用ID
	 */
	@Schema(description =  "操作者名称")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String operatorName;
	/**
	 * 应用ID
	 */
	@Schema(description =  "日志信息")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
	private String message;

	public AfterSaleLog(String sn, String operatorId, String operatorType, String operatorName,
		String message) {
		this.sn = sn;
		this.operatorId = operatorId;
		this.operatorType = operatorType;
		this.operatorName = operatorName;
		this.message = message;
	}
}
