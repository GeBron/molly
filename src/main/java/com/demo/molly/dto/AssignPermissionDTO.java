package com.demo.molly.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分配权限请求参数
 */
public class AssignPermissionDTO {

    @NotNull(message = "权限ID列表不能为空")
    private List<Long> permissionIds;

    public AssignPermissionDTO() {
    }

    public AssignPermissionDTO(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }

    public List<Long> permissionIds() {
        return permissionIds;
    }

    public List<Long> getPermissionIds() {
        return permissionIds;
    }

    public void setPermissionIds(List<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}
