package com.demo.molly.projection;

import com.demo.molly.entity.Role;

import java.util.List;

/**
 * 角色及其权限投影（将关联字段从 Role 实体中分离）
 */
public class RoleWithPermissions extends Role {

    private List<Long> permissionIds;

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
