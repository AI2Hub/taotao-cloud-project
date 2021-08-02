package com.taotao.cloud.demo.seata.business.service;

import com.taotao.cloud.demo.seata.business.feign.OrderFeignClient;
import com.taotao.cloud.demo.seata.business.feign.StorageFeignClient;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 业务
 *
 * @author zlt
 * @since 2019/9/14
 */
@Service
public class BusinessService {
	private static final String COMMODITY_CODE = "P001";
	private static final int ORDER_COUNT = 1;

	@Resource
	private OrderFeignClient orderFeignClient;

	@Resource
	private StorageFeignClient storageFeignClient;

	/**
	 * 下订单
	 */
	@GlobalTransactional
	public void placeOrder(String userId) {
		storageFeignClient.deduct(COMMODITY_CODE, ORDER_COUNT);

		orderFeignClient.create(userId, COMMODITY_CODE, ORDER_COUNT);
	}
}
