package com.demo.molly.entity;

/**
 * 角色权限关联实体
 */
public class RolePermission extends BaseEntity {

    private Long roleId;
    private Long permissionId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
}
