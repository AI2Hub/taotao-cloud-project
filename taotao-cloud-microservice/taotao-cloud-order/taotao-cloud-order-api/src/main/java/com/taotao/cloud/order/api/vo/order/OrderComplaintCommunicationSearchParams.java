package com.taotao.cloud.order.api.vo.order;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单投诉搜索参数
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单投诉搜索参数")
public class OrderComplaintCommunicationSearchParams {

	@Schema(description = "投诉id")
	private String complainId;

	@Schema(description = "所属，买家/卖家")
	private String owner;

	@Schema(description = "对话所属名称")
	private String ownerName;

	@Schema(description = "对话所属id,卖家id/买家id")
	private String ownerId;

//	public LambdaQueryWrapper<OrderComplaintCommunication> lambdaQueryWrapper() {
//		LambdaQueryWrapper<OrderComplaintCommunication> queryWrapper = new LambdaQueryWrapper<>();
//		if (StrUtil.isNotEmpty(complainId)) {
//			queryWrapper.eq(OrderComplaintCommunication::getComplainId, complainId);
//		}
//		if (StrUtil.isNotEmpty(owner)) {
//			queryWrapper.eq(OrderComplaintCommunication::getOwner, owner);
//		}
//		if (StrUtil.isNotEmpty(ownerName)) {
//			queryWrapper.eq(OrderComplaintCommunication::getOwnerName, ownerName);
//		}
//		if (StrUtil.isNotEmpty(ownerId)) {
//			queryWrapper.eq(OrderComplaintCommunication::getOwnerId, ownerId);
//		}
//		return queryWrapper;
//	}

}
