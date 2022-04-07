package com.taotao.cloud.order.biz.service.cart.render.impl;

import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnums;
import com.taotao.cloud.order.api.vo.cart.CartSkuVO;
import com.taotao.cloud.order.api.vo.cart.CartVO;
import com.taotao.cloud.order.biz.service.cart.render.CartRenderStep;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 佣金计算
 */
@Service
public class CheckedFilterRender implements CartRenderStep {

	@Override
	public RenderStepEnums step() {
		return RenderStepEnums.CHECKED_FILTER;
	}

	@Override
	public void render(TradeDTO tradeDTO) {
		//将购物车到sku未选择信息过滤
		List<CartSkuVO> collect = tradeDTO.getSkuList().parallelStream()
			.filter(i -> Boolean.TRUE.equals(i.getChecked())).collect(Collectors.toList());
		tradeDTO.setSkuList(collect);

		//购物车信息过滤
		List<CartVO> cartVOList = new ArrayList<>();
		//循环购物车信息
		for (CartVO cartVO : tradeDTO.getCartList()) {
			//如果商品选中，则加入到对应购物车
			cartVO.setSkuList(cartVO.getCheckedSkuList());
			cartVOList.add(cartVO);
		}
		tradeDTO.setCartList(cartVOList);
	}


}
