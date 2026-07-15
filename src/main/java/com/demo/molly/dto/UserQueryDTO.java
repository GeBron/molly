package com.demo.molly.dto;

/**
 * 用户查询参数
 */
public class UserQueryDTO {

    private String username;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;

    public UserQueryDTO() {
    }

    public UserQueryDTO(String username, Integer status, Integer pageNum, Integer pageSize) {
        this.username = username;
        this.status = status;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
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
}
