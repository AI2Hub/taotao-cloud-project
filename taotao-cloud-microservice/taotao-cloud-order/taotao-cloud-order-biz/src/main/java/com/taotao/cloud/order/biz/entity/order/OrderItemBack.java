package com.taotao.cloud.order.biz.entity.order;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.*;

/**
 * 订单明细表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderItemBack.TABLE_NAME)
@Table(name = OrderItemBack.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderItemBack.TABLE_NAME, comment = "订单明细表")
public class OrderItemBack extends BaseSuperEntity<OrderItemBack,Long> {

	public static final String TABLE_NAME = "order_item";

	/**
	 * 订单子编码
	 */
	@Column(name = "item_code", unique = true, columnDefinition = "varchar(32) not null comment '订单子编码'")
	private String itemCode;

	/**
	 * 商品SPU ID
	 */
	@Column(name = "product_spu_id", columnDefinition = "bigint not null comment '商品SPU ID'")
	private Long productSpuId;

	/**
	 * 商品SPU_CODE
	 */
	@Column(name = "product_spu_code", columnDefinition = "varchar(32) not null comment '商品SPU CODE'")
	private String productSpuCode;

	/**
	 * 商品SPU名称
	 */
	@Column(name = "product_spu_name", columnDefinition = "varchar(32) not null comment '商品SPU名称'")
	private String productSpuName;

	/**
	 * 商品SKU ID
	 */
	@Column(name = "product_sku_id", columnDefinition = "bigint not null comment '商品SKU ID'")
	private Long productSkuId;

	/**
	 * 商品SKU 规格名称
	 */
	@Column(name = "product_sku_name", columnDefinition = "varchar(255) not null comment '商品SKU 规格名称'")
	private String productSkuName;

	/**
	 * 商品单价
	 */
	@Column(name = "product_price", columnDefinition = "decimal(10,2) not null default 0 comment '商品单价'")
	private BigDecimal productPrice = BigDecimal.ZERO;

	/**
	 * 购买数量
	 */
	@Column(name = "num", columnDefinition = "int not null default 1 comment '购买数量'")
	private Integer num = 1;

	/**
	 * 合计金额
	 */
	@Column(name = "sum_amount", columnDefinition = "decimal(10,2) not null default 0 comment '合计金额'")
	private BigDecimal sumAmount = BigDecimal.ZERO;

	/**
	 * 商品主图
	 */
	@Column(name = "product_pic_url", columnDefinition = "varchar(255) comment '商品主图'")
	private String productPicUrl;

	/**
	 * 供应商id
	 */
	@Column(name = "supplier_id", columnDefinition = "bigint not null comment '供应商id'")
	private Long supplierId;

	/**
	 * 供应商名称
	 */
	@Column(name = "supplier_name", columnDefinition = "varchar(255) not null comment '供应商名称'")
	private String supplierName;

	/**
	 * 超时退货期限
	 */
	@Column(name = "refund_time", columnDefinition = "int default 0 comment '超时退货期限'")
	private Integer refundTime;

	/**
	 * 退货数量
	 */
	@Column(name = "reject_count", columnDefinition = "int not null default 0 comment '退货数量'")
	private Integer rejectCount = 0;

	/**
	 * 商品类型 0 普通商品 1 秒杀商品
	 */
	@Column(name = "type", columnDefinition = "int not null default 0 comment '0-普通商品 1-秒杀商品'")
	private Integer type = 0;
}
