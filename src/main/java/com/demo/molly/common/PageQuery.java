package com.demo.molly.common;

/**
 * 分页查询基类
 */
public class PageQuery {

    private Integer pageNum = 1;
    private Integer pageSize = 10;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int offset() {
        return (Math.max(pageNum, 1) - 1) * Math.max(pageSize, 1);
    }

    public int limit() {
        return Math.max(pageSize, 1);
    }
}
