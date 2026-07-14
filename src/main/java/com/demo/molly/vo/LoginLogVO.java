package com.demo.molly.vo;

import java.time.LocalDateTime;

/**
 * 登录日志视图
 */
public record LoginLogVO(
        Long id,
        Long userId,
        String username,
        String ip,
        String operation,
        String status,
        String message,
        LocalDateTime createdAt
) {
}
