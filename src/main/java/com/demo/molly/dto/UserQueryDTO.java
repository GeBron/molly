package com.demo.molly.dto;

import com.demo.molly.common.PageQuery;

/**
 * 用户查询参数
 */
public class UserQueryDTO extends PageQuery {

    private String username;
    private Integer status;

    public UserQueryDTO() {
    }

    public UserQueryDTO(String username, Integer status, Integer pageNum, Integer pageSize) {
        this.username = username;
        this.status = status;
        setPageNum(pageNum);
        setPageSize(pageSize);
    }

    public String username() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer status() {
        return status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
