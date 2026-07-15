package com.demo.molly.vo;

import java.time.LocalDateTime;

/**
 * 操作日志视图
 */
public record OperationLogVO(
        Long id,
        Long userId,
        String username,
        String module,
        String operation,
        String requestUrl,
        String requestMethod,
        String method,
        String params,
        String result,
        Long duration,
        String ip,
        LocalDateTime createdAt
) {
}
