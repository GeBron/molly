package com.demo.molly.controller;

import com.demo.molly.common.PageResult;
import com.demo.molly.common.Result;
import com.demo.molly.dto.LogQueryDTO;
import com.demo.molly.service.LogService;
import com.demo.molly.vo.LoginLogVO;
import com.demo.molly.vo.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志控制器
 */
@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PreAuthorize("hasAuthority('loginLog:view')")
    @GetMapping("/login")
    public Result<PageResult<LoginLogVO>> loginLogs(LogQueryDTO query) {
        return Result.success(logService.loginLogList(query));
    }

    @PreAuthorize("hasAuthority('operationLog:view')")
    @GetMapping("/operation")
    public Result<PageResult<OperationLogVO>> operationLogs(LogQueryDTO query) {
        return Result.success(logService.operationLogList(query));
    }
}
