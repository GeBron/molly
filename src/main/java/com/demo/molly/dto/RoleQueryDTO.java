package com.demo.molly.dto;

/**
 * 角色查询参数
 */
public class RoleQueryDTO {

    private String roleName;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;

    public RoleQueryDTO() {
    }

    public RoleQueryDTO(String roleName, Integer status, Integer pageNum, Integer pageSize) {
        this.roleName = roleName;
        this.status = status;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public String roleName() {
        return roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
