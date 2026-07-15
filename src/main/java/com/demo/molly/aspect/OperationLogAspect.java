package com.demo.molly.aspect;

import com.demo.molly.common.Result;
import com.demo.molly.entity.OperationLog;
import com.demo.molly.mapper.OperationLogMapper;
import com.demo.molly.security.LoginUser;
import com.demo.molly.util.IpUtil;
import com.demo.molly.util.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {

    private final OperationLogMapper operationLogMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public OperationLogAspect(OperationLogMapper operationLogMapper, ObjectMapper objectMapper) {
        this.operationLogMapper = operationLogMapper;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, OperationLog operationLog) throws Throwable {
        long start = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        Object result = null;
        Throwable throwable = null;
        try {
            result = point.proceed();
            return result;
        } catch (Throwable t) {
            throwable = t;
            throw t;
        } finally {
            long duration = System.currentTimeMillis() - start;
            saveLog(point, operationLog, request, result, throwable, duration);
        }
    }

    private void saveLog(ProceedingJoinPoint point, OperationLog operationLog, HttpServletRequest request,
                         Object result, Throwable throwable, long duration) {
        OperationLog log = new OperationLog();
        LoginUser loginUser = SecurityUtil.getCurrentUser();
        if (loginUser != null) {
            log.setUserId(loginUser.getUser().getId());
            log.setUsername(loginUser.getUsername());
        }
        log.setModule(operationLog.module());
        log.setOperation(operationLog.operation());
        if (request != null) {
            log.setRequestUrl(request.getRequestURI());
            log.setRequestMethod(request.getMethod());
            log.setIp(IpUtil.getIp(request));
        }
        log.setMethod(point.getSignature().toShortString());
        try {
            log.setParams(objectMapper.writeValueAsString(point.getArgs()));
            if (throwable != null) {
                log.setResult(throwable.getMessage());
            } else if (result instanceof Result<?> r && r.getData() != null) {
                log.setResult(objectMapper.writeValueAsString(r.getData()));
            } else {
                log.setResult(objectMapper.writeValueAsString(result));
            }
        } catch (Exception e) {
            log.setParams("参数序列化失败");
            log.setResult("结果序列化失败");
        }
        log.setDuration(duration);
        operationLogMapper.insert(log);
    }
}
