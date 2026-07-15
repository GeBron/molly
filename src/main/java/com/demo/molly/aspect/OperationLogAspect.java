package com.demo.molly.aspect;

import com.demo.molly.common.Result;
import com.demo.molly.entity.OperationLog;
import com.demo.molly.security.LoginUser;
import com.demo.molly.service.AsyncLogService;
import com.demo.molly.util.IpUtil;
import com.demo.molly.util.SecurityUtil;
import com.demo.molly.util.SensitiveDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

/**
 * 操作日志切面
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final int DEFAULT_MAX_LENGTH = 10 * 1024;

    private final AsyncLogService asyncLogService;
    private final ObjectMapper objectMapper;

    @Autowired
    public OperationLogAspect(AsyncLogService asyncLogService, ObjectMapper objectMapper) {
        this.asyncLogService = asyncLogService;
        this.objectMapper = objectMapper;
    }

    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint point, com.demo.molly.aspect.OperationLog operationLog) throws Throwable {
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

    private void saveLog(ProceedingJoinPoint point, com.demo.molly.aspect.OperationLog operationLog, HttpServletRequest request,
                         Object result, Throwable throwable, long duration) {
        OperationLog log = new OperationLog();
        LoginUser loginUser = SecurityUtil.getCurrentUser();
        if (loginUser != null) {
            log.setUserId(loginUser.getUser().getId());
            log.setUsername(loginUser.getUsername());
        }
        log.setModule(operationLog.module());
        log.setOperation(operationLog.operation());
        log.setStatus(throwable == null ? 1 : 0);
        if (request != null) {
            log.setRequestUrl(request.getRequestURI());
            log.setRequestMethod(request.getMethod());
            log.setIp(IpUtil.getIp(request));
        }
        log.setMethod(point.getSignature().toShortString());
        log.setDuration(duration);

        if (operationLog.saveParams()) {
            String params = serializeParams(point.getArgs(), operationLog);
            log.setParams(truncate(params, operationLog.maxParamLength()));
        }
        if (operationLog.saveResult()) {
            String resultStr = serializeResult(result, throwable);
            log.setResult(truncate(resultStr, operationLog.maxResultLength()));
        }

        Long operatorId = loginUser != null ? loginUser.getUser().getId() : 0L;
        log.setCreatedBy(operatorId);
        log.setUpdatedBy(operatorId);

        asyncLogService.saveOperationLog(log);
    }

    private String serializeParams(Object[] args, com.demo.molly.aspect.OperationLog operationLog) {
        Object[] filtered = Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest)
                        && !(arg instanceof HttpServletResponse)
                        && !(arg instanceof MultipartFile)
                        && !(arg instanceof MultipartFile[]))
                .toArray();
        try {
            String json = objectMapper.writeValueAsString(filtered);
            return SensitiveDataUtil.maskJson(json, objectMapper, operationLog.sensitiveFields());
        } catch (Exception e) {
            return "参数序列化失败";
        }
    }

    private String serializeResult(Object result, Throwable throwable) {
        try {
            if (throwable != null) {
                return throwable.getMessage();
            }
            if (result instanceof Result<?> r && r.getData() != null) {
                return SensitiveDataUtil.maskJson(objectMapper.writeValueAsString(r.getData()), objectMapper);
            }
            return SensitiveDataUtil.maskJson(objectMapper.writeValueAsString(result), objectMapper);
        } catch (Exception e) {
            return "结果序列化失败";
        }
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return null;
        }
        int limit = maxLength > 0 ? maxLength : DEFAULT_MAX_LENGTH;
        return value.length() > limit ? value.substring(0, limit) : value;
    }
}
