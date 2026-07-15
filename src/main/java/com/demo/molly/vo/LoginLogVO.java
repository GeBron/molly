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

    public String ip() {
        return ip;
    }

    public String getIp() {
        return ip;
    }

    public String operation() {
        return operation;
    }

    public String getOperation() {
        return operation;
    }

    public String status() {
        return status;
    }

    public String getStatus() {
        return status;
    }

    public String message() {
        return message;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
