package com.demo.molly.vo;

import java.time.LocalDateTime;

/**
 * 登录日志视图
 */
public class LoginLogVO {

    private final Long id;
    private final Long userId;
    private final String username;
    private final String ip;
    private final String operation;
    private final String status;
    private final String message;
    private final LocalDateTime createdAt;

    public LoginLogVO(Long id, Long userId, String username, String ip, String operation, String status, String message, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.ip = ip;
        this.operation = operation;
        this.status = status;
        this.message = message;
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

    public String getIp() {
        return ip;
    }

    public String getOperation() {
        return operation;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
