package com.demo.molly.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色视图
 */
public class RoleVO {

    private final Long id;
    private final String roleCode;
    private final String roleName;
    private final Integer status;
    private final LocalDateTime createdAt;
    private final List<Long> permissionIds;

    public RoleVO(Long id, String roleCode, String roleName, Integer status, LocalDateTime createdAt, List<Long> permissionIds) {
        this.id = id;
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.status = status;
        this.createdAt = createdAt;
        this.permissionIds = permissionIds;
    }

    public Long id() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public String roleCode() {
        return roleCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String roleName() {
        return roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public Integer status() {
        return status;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Long> permissionIds() {
        return permissionIds;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }
}
