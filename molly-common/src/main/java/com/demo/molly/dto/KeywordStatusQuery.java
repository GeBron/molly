package com.demo.molly.dto;

import com.demo.molly.common.PageQuery;

/**
 * 通用关键字+状态分页查询参数
 */
public class KeywordStatusQuery extends PageQuery {

    private String keyword;
    private Integer status;

    public KeywordStatusQuery() {
    }

    public KeywordStatusQuery(String keyword, Integer status, Integer pageNum, Integer pageSize) {
        this.keyword = keyword;
        this.status = status;
        setPageNum(pageNum);
        setPageSize(pageSize);
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
