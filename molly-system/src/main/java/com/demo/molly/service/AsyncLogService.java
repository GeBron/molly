package com.demo.molly.service;

import com.demo.molly.entity.OperationLog;

/**
 * 异步日志服务接口
 */
public interface AsyncLogService {

    void saveOperationLog(OperationLog log);
}
