package com.demo.molly.service.impl;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.LogQueryDTO;
import com.demo.molly.entity.LoginLog;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.demo.molly.entity.OperationLog;
import com.demo.molly.mapper.LoginLogMapper;
import com.demo.molly.mapper.OperationLogMapper;
import com.demo.molly.service.LogService;
import com.demo.molly.vo.LoginLogVO;
import com.demo.molly.vo.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志服务
 */
@Service
public class LogServiceImpl implements LogService {

    private final LoginLogMapper loginLogMapper;
    private final OperationLogMapper operationLogMapper;

    @Autowired
    public LogServiceImpl(LoginLogMapper loginLogMapper, OperationLogMapper operationLogMapper) {
        this.loginLogMapper = loginLogMapper;
        this.operationLogMapper = operationLogMapper;
    }

    public PageResult<LoginLogVO> loginLogList(LogQueryDTO query) {
        PageInfo<LoginLog> pageInfo = PageHelper.startPage(query.getPageNum(), query.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        loginLogMapper.selectList(query.getStartTime(), query.getEndTime());
                    }
                });

        List<LoginLogVO> list = new ArrayList<>();
        for (LoginLog log : pageInfo.getList()) {
            list.add(new LoginLogVO(
                    log.getId(),
                    log.getUserId(),
                    log.getUsername(),
                    log.getIp(),
                    log.getOperation(),
                    log.getStatus(),
                    log.getMessage(),
                    log.getCreatedAt()));
        }
        return new PageResult<>(list, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
    }

    public PageResult<OperationLogVO> operationLogList(LogQueryDTO query) {
        PageInfo<OperationLog> pageInfo = PageHelper.startPage(query.getPageNum(), query.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        operationLogMapper.selectList(query.getStartTime(), query.getEndTime());
                    }
                });

        List<OperationLogVO> list = new ArrayList<>();
        for (OperationLog log : pageInfo.getList()) {
            list.add(new OperationLogVO(
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
                    log.getCreatedAt()));
        }
        return new PageResult<>(list, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
    }
}
