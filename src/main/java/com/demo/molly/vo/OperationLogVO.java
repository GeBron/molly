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

    public Long id() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public Long userId() {
        return userId;
    }

    public Long getUserId() {
        return userId;
    }

    public String username() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public String module() {
        return module;
    }

    public String getModule() {
        return module;
    }

    public String operation() {
        return operation;
    }

    public String getOperation() {
        return operation;
    }

    public String requestUrl() {
        return requestUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public String requestMethod() {
        return requestMethod;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String method() {
        return method;
    }

    public String getMethod() {
        return method;
    }

    public String params() {
        return params;
    }

    public String getParams() {
        return params;
    }

    public String result() {
        return result;
    }

    public String getResult() {
        return result;
    }

    public Long duration() {
        return duration;
    }

    public Long getDuration() {
        return duration;
    }

    public String ip() {
        return ip;
    }

    public String getIp() {
        return ip;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
