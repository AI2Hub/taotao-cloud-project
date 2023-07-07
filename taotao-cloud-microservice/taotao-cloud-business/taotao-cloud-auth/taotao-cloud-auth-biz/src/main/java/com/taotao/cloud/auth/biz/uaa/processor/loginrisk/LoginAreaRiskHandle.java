package com.taotao.cloud.auth.biz.uaa.processor.loginrisk;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录地区风险实现
 */
@Component
public class LoginAreaRiskHandle extends AbstractLoginHandle {

	private static final Integer SEC = 1;

	private static final Integer MIN = 2;

	private static final Integer HOU = 3;

	//@Resource
	//private LoginLogService loginLogService;

	@Override
	public void filterRisk(List<RiskRule> filter, Map<Integer, RiskRule> ruleMap, UserAccount account) {
		if (MapUtil.isNotEmpty(ruleMap)) {
			RiskRule areaRisk = ruleMap.get(4);
			if (null != areaRisk) {
				Integer triggerTime = areaRisk.getTriggerTime();
				Integer triggerTimeType = areaRisk.getTriggerTimeType();
				Integer triggerNumber = areaRisk.getTriggerNumber();
				Date endTime = new Date();
				Date startTime;
				//获取查询时间范围的开始时间
				if (triggerTimeType == SEC) {
					startTime = DateUtil.offsetSecond(endTime, -triggerTime);
				} else if (triggerTimeType == MIN) {
					startTime = DateUtil.offsetMinute(endTime, -triggerTime);
				} else {
					startTime = DateUtil.offsetHour(endTime, -triggerTime);
				}
				// 指定时间范围内，登录地区是否超过指定个数
				//List<LoginLog> loginLogList = loginLogService.lambdaQuery()
				//	.select(LoginLog::getCityCode).between(LoginLog::getTime, startTime, endTime)
				//	.eq(LoginLog::getResult, 1)
				//	.eq(LoginLog::getAccount, account.getAccount())
				//	.list();
				//long areaCount = CollUtil.emptyIfNull(loginLogList).stream().map(LoginLog::getCityCode).distinct().count();
				long areaCount = 0;
				//如果超过指定个数，则将该风险策略添加到filter
				if (areaCount >= triggerNumber.longValue()) {
					filter.add(areaRisk);
				}
			}
		}
		if (this.nextHandle != null) {
			this.nextHandle.filterRisk(filter, ruleMap, account);
		}
	}
}
