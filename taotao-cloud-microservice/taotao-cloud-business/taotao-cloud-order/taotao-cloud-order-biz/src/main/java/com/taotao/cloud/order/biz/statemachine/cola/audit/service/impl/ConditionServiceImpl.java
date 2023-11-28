package com.taotao.cloud.order.biz.statemachine.cola.audit.service.impl;

import com.alibaba.cola.statemachine.Condition;
import com.taotao.cloud.order.biz.statemachine.cola.audit.service.ConditionService;
import org.springframework.stereotype.Component;

/**
 * @date 2023/7/12 17:50
 */
@Component
public class ConditionServiceImpl implements ConditionService {
	@Override
	public Condition<AuditContext> passOrRejectCondition() {
		return context -> {
			System.out.println(1);
			return true;
		};
	}

	@Override
	public Condition<AuditContext> doneCondition() {
		return context -> {
			System.out.println(1);
			return true;
		};
	}
}
