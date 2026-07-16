package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.LogQueryDTO;
import com.demo.molly.entity.LoginLog;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.demo.molly.entity.OperationLog;
import com.demo.molly.mapper.LoginLogMapper;
import com.demo.molly.mapper.OperationLogMapper;
import com.demo.molly.vo.LoginLogVO;
import com.demo.molly.vo.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<LoginLog> logs = loginLogMapper.selectList(query.startTime(), query.endTime());
        PageInfo<LoginLog> pageInfo = new PageInfo<>(logs);

        List<LoginLogVO> list = pageInfo.getList().stream()
                .map(log -> new LoginLogVO(
                        log.getId(),
                        log.getUserId(),
                        log.getUsername(),
                        log.getIp(),
                        log.getOperation(),
                        log.getStatus(),
                        log.getMessage(),
                        log.getCreatedAt()))
                .collect(Collectors.toList());
        return new PageResult<>(list, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
    }

    public PageResult<OperationLogVO> operationLogList(LogQueryDTO query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<OperationLog> logs = operationLogMapper.selectList(query.startTime(), query.endTime());
        PageInfo<OperationLog> pageInfo = new PageInfo<>(logs);

        List<OperationLogVO> list = pageInfo.getList().stream()
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
                .collect(Collectors.toList());
        return new PageResult<>(list, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
    }
}
