package com.demo.molly.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 角色请求参数
 */
public class RoleDTO {

    @NotBlank(message = "角色编码不能为空")
    @Size(max = 64, message = "角色编码长度不能超过64")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 64, message = "角色名称长度不能超过64")
    private String roleName;

    private Integer status;

    public RoleDTO() {
    }

    public RoleDTO(String roleCode, String roleName, Integer status) {
        this.roleCode = roleCode;
        this.roleName = roleName;
        this.status = status;
    }

    public String roleCode() {
        return roleCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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
