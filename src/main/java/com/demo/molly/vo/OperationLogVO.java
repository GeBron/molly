package com.demo.molly.vo;

import java.time.LocalDateTime;

/**
 * 操作日志视图
 */
public class OperationLogVO {

    private final Long id;
    private final Long userId;
    private final String username;
    private final String module;
    private final String operation;
    private final String requestUrl;
    private final String requestMethod;
    private final String method;
    private final String params;
    private final String result;
    private final Long duration;
    private final String ip;
    private final LocalDateTime createdAt;

    public OperationLogVO(Long id, Long userId, String username, String module, String operation,
                          String requestUrl, String requestMethod, String method, String params,
                          String result, Long duration, String ip, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.module = module;
        this.operation = operation;
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.method = method;
        this.params = params;
        this.result = result;
        this.duration = duration;
        this.ip = ip;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getModule() {
        return module;
    }

    public String getOperation() {
        return operation;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getMethod() {
        return method;
    }

    public String getParams() {
        return params;
    }

    public String getResult() {
        return result;
    }

    public Long getDuration() {
        return duration;
    }

    public String getIp() {
        return ip;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
