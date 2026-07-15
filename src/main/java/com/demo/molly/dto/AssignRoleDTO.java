package com.demo.molly.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分配角色请求参数
 */
public class AssignRoleDTO {

    @NotNull(message = "角色ID列表不能为空")
    private List<Long> roleIds;

    public AssignRoleDTO() {
    }

    public AssignRoleDTO(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public List<Long> roleIds() {
        return roleIds;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
