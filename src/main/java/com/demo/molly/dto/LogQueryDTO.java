package com.demo.molly.dto;

import java.time.LocalDateTime;

/**
 * 日志查询参数
 */
public class LogQueryDTO {

    private Integer pageNum;
    private Integer pageSize;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public LogQueryDTO() {
    }

    public LogQueryDTO(Integer pageNum, Integer pageSize, LocalDateTime startTime, LocalDateTime endTime) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer pageNum() {
        return pageNum;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer pageSize() {
        return pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
