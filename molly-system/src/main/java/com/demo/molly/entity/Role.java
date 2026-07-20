package com.demo.molly.entity;

/**
 * 角色实体（不包含关联字段，关联数据通过投影对象承载）
 */
public class Role extends BaseEntity {

    private String roleCode;
    private String roleName;
    private Integer status;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
