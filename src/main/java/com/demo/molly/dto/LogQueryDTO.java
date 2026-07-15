package com.demo.molly.dto;

import java.time.LocalDateTime;

/**
 * 日志查询参数
 */
public record LogQueryDTO(
        Integer pageNum,
        Integer pageSize,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
