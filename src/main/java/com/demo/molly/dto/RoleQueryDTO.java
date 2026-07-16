package com.demo.molly.dto;

import com.demo.molly.common.PageQuery;

/**
 * 角色查询参数
 */
public class RoleQueryDTO extends PageQuery {

    private String roleName;
    private Integer status;

    public RoleQueryDTO() {
    }

    public RoleQueryDTO(String roleName, Integer status, Integer pageNum, Integer pageSize) {
        this.roleName = roleName;
        this.status = status;
        setPageNum(pageNum);
        setPageSize(pageSize);
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
}
