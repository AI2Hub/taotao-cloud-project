/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 支付宝配置表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:22
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = AlipayConfig.TABLE_NAME)
@TableName(AlipayConfig.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = AlipayConfig.TABLE_NAME, comment = "支付宝配置表")
public class AlipayConfig extends BaseSuperEntity<AlipayConfig, Long> {

	public static final String TABLE_NAME = "tt_alipay_config";

	/**
	 * 应用ID
	 */
	@Column(name = "app_id", nullable = false, columnDefinition = "varchar(64) not null comment '应用ID'")
	private String appId;

	/**
	 * 编码
	 */
	@Column(name = "charset", nullable = false, columnDefinition = "varchar(32) not null comment '编码'")
	private String charset;


	/**
	 * 类型 固定格式json
	 */
	@Column(name = "format", nullable = false, columnDefinition = "varchar(256) not null comment '类型 固定格式json'")
	private String format;

	/**
	 * 网关地址
	 */
	@Column(name = "gateway_url", nullable = false, columnDefinition = "varchar(256) not null comment '网关地址'")
	private String gatewayUrl;

	/**
	 * 异步回调
	 */
	@Column(name = "notify_url", nullable = false, columnDefinition = "varchar(256) not null comment '异步回调'")
	private String notifyUrl;

	/**
	 * 私钥
	 */
	@Column(name = "private_key", nullable = false, columnDefinition = "varchar(64) not null comment '私钥'")
	private String privateKey;

	/**
	 * 公钥
	 */
	@Column(name = "public_key", nullable = false, columnDefinition = "varchar(64) not null comment '公钥'")
	private String publicKey;

	/**
	 * 回调地址
	 */
	@Column(name = "return_url", nullable = false, columnDefinition = "varchar(256) not null comment '回调地址'")
	private String returnUrl;

	/**
	 * 签名方式
	 */
	@Column(name = "sign_type", nullable = false, columnDefinition = "varchar(32) not null comment '签名方式'")
	private String signType;

	/**
	 * 商户号
	 */
	@Column(name = "sys_service_provider_id", nullable = false, columnDefinition = "varchar(32) not null comment '商户号'")
	private String sysServiceProviderId;
}
