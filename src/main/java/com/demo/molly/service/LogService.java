package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.LogQueryDTO;
import com.demo.molly.entity.LoginLog;
import com.demo.molly.entity.OperationLog;
import com.demo.molly.mapper.LoginLogMapper;
import com.demo.molly.mapper.OperationLogMapper;
import com.demo.molly.vo.LoginLogVO;
import com.demo.molly.vo.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志服务
 */
@Service
public class LogService {

    private final LoginLogMapper loginLogMapper;
    private final OperationLogMapper operationLogMapper;

    @Autowired
    public LogService(LoginLogMapper loginLogMapper, OperationLogMapper operationLogMapper) {
        this.loginLogMapper = loginLogMapper;
        this.operationLogMapper = operationLogMapper;
    }

    public PageResult<LoginLogVO> loginLogList(LogQueryDTO query) {
        int pageNum = query.pageNum() == null ? 1 : query.pageNum();
        int pageSize = query.pageSize() == null ? 10 : query.pageSize();
        int offset = (pageNum - 1) * pageSize;

        List<LoginLog> logs = loginLogMapper.selectList(query.startTime(), query.endTime(), offset, pageSize);
        long total = loginLogMapper.count(query.startTime(), query.endTime());

        List<LoginLogVO> list = logs.stream()
                .map(log -> new LoginLogVO(
                        log.getId(),
                        log.getUserId(),
                        log.getUsername(),
                        log.getIp(),
                        log.getOperation(),
                        log.getStatus(),
                        log.getMessage(),
                        log.getCreatedAt()))
                .toList();
        return new PageResult<>(list, total, pageNum, pageSize);
    }

    public PageResult<OperationLogVO> operationLogList(LogQueryDTO query) {
        int pageNum = query.pageNum() == null ? 1 : query.pageNum();
        int pageSize = query.pageSize() == null ? 10 : query.pageSize();
        int offset = (pageNum - 1) * pageSize;

        List<OperationLog> logs = operationLogMapper.selectList(query.startTime(), query.endTime(), offset, pageSize);
        long total = operationLogMapper.count(query.startTime(), query.endTime());

        List<OperationLogVO> list = logs.stream()
                .map(log -> new OperationLogVO(
                        log.getId(),
                        log.getUserId(),
                        log.getUsername(),
                        log.getModule(),
                        log.getOperation(),
                        log.getRequestUrl(),
                        log.getRequestMethod(),
                        log.getMethod(),
                        log.getParams(),
                        log.getResult(),
                        log.getDuration(),
                        log.getIp(),
                        log.getCreatedAt()))
                .toList();
        return new PageResult<>(list, total, pageNum, pageSize);
    }
}
