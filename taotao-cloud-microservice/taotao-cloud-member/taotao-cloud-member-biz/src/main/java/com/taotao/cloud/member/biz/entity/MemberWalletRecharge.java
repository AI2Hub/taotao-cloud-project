package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 预存款充值记录表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:39:33
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberWalletRecharge.TABLE_NAME)
@TableName(MemberWalletRecharge.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberWalletRecharge.TABLE_NAME, comment = "预存款充值记录表")
public class MemberWalletRecharge extends BaseSuperEntity<MemberWalletRecharge, Long> {

	public static final String TABLE_NAME = "tt_member_wallet_recharge";

	/**
	 * 充值订单编号
	 */
	@Column(name = "recharge_sn", nullable = false, columnDefinition = "varchar(32) not null comment '充值订单编号'")
	private String rechargeSn;

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	/**
	 * 充值金额
	 */
	@Column(name = "recharge_money", nullable = false, columnDefinition = "decimal(10,2) not null comment '充值金额'")
	private BigDecimal rechargeMoney;

	/**
	 * 充值方式，如：支付宝，微信
	 */
	@Column(name = "recharge_way", nullable = false, columnDefinition = "varchar(32) not null comment '充值方式，如：支付宝，微信'")
	private String rechargeWay;

	/**
	 * 支付状态
	 */
	@Column(name = "pay_status", nullable = false, columnDefinition = "varchar(32) not null comment '支付状态'")
	private String payStatus;

	/**
	 * 支付插件id
	 */
	@Column(name = "payment_plugin_id", nullable = false, columnDefinition = "varchar(32) not null comment '支付插件id'")
	private String paymentPluginId;

	/**
	 * 第三方流水
	 */
	@Column(name = "receivable_no", nullable = false, columnDefinition = "varchar(32) not null comment '第三方流水'")
	private String receivableNo;

	/**
	 * 支付时间
	 */
	@Column(name = "pay_time", nullable = false, columnDefinition = "TIMESTAMP  comment '支付时间'")
	private LocalDateTime payTime;

	/**
	 * 构建充值账单信息
	 *
	 * @param rechargeSn 充值订单号
	 * @param memberId   会员id
	 * @param memberName 会员名称
	 * @param money      充值金额
	 */
	//public Recharge(String rechargeSn, String memberId, String memberName, Double money) {
	//	this.rechargeSn = rechargeSn;
	//	this.memberId = memberId;
	//	this.memberName = memberName;
	//	this.rechargeMoney = money;
	//	this.payStatus = PayStatusEnum.UNPAID.name();
	//}
}
