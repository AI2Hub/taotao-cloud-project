package com.taotao.cloud.job.biz.quartz1.service;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.job.biz.quartz1.entity.QuartzJobLogEntity;
import com.taotao.cloud.job.biz.quartz1.param.QuartzJobLogQuery;
import com.taotao.cloud.job.biz.quartz1.vo.QuartzJobLogVO;

/**
 * 定时任务日志
 */
public interface QuartzJobLogService {

	/**
	 * 添加
	 */
	public void add(QuartzJobLogEntity quartzJobLog);

	/**
	 * 分页
	 */
	public PageResult<QuartzJobLogVO> page(QuartzJobLogQuery quartzJobLogQuery);

	/**
	 * 单条
	 */
	public QuartzJobLogVO findById(Long id);


}
