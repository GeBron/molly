package com.demo.molly.service;

import com.demo.molly.entity.OperationLog;
import com.demo.molly.mapper.OperationLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步日志服务
 */
@Service
public class AsyncLogService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncLogService.class);

    private final OperationLogMapper operationLogMapper;

    @Autowired
    public AsyncLogService(OperationLogMapper operationLogMapper) {
        this.operationLogMapper = operationLogMapper;
    }

    @Async
    public void saveOperationLog(OperationLog log) {
        try {
            operationLogMapper.insert(log);
        } catch (Exception e) {
            logger.error("保存操作日志失败", e);
        }
    }
}
