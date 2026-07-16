package com.demo.molly.dto;

import com.demo.molly.common.PageQuery;

import java.time.LocalDateTime;

/**
 * 日志查询参数
 */
public class LogQueryDTO extends PageQuery {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public LogQueryDTO() {
    }

    public LogQueryDTO(Integer pageNum, Integer pageSize, LocalDateTime startTime, LocalDateTime endTime) {
        setPageNum(pageNum);
        setPageSize(pageSize);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime startTime() {
        return startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime endTime() {
        return endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
