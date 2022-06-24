package com.taotao.cloud.logger.mztlog.service;



import com.taotao.cloud.logger.mztlog.beans.LogRecord;

import java.util.List;

public interface ILogRecordService {
    /**
     * 保存log
     *
     * @param logRecord 日志实体
     */
    void record(LogRecord logRecord);

    /**
     * <p>批量保存log</p>
     * @param records 日志实体列表
     * */
    void batchRecord(List<LogRecord> records);

    /**
     * 返回最多100条记录
     *
     * @param type  操作日志类型
     * @param bizNo 操作日志的业务标识，比如：订单号
     * @return 操作日志列表
     */
    List<LogRecord> queryLog(String bizNo, String type);

    /**
     * 返回最多100条记录
     *
     * @param type    操作日志类型
     * @param subType 操作日志子类型
     * @param bizNo   操作日志的业务标识，比如：订单号
     * @return 操作日志列表
     */
    List<LogRecord> queryLogByBizNo(String bizNo, String type, String subType);
}
