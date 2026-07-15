package com.demo.molly.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 权限请求参数
 */
public class PermissionDTO {

    @NotBlank(message = "权限编码不能为空")
    @Size(max = 128, message = "权限编码长度不能超过128")
    private String permCode;

    @NotBlank(message = "权限名称不能为空")
    @Size(max = 64, message = "权限名称长度不能超过64")
    private String permName;

    @NotNull(message = "权限类型不能为空")
    private Integer type;

    private Long parentId;

    @Size(max = 255, message = "路径长度不能超过255")
    private String path;

    private Integer sort;

    private Integer status;

    public PermissionDTO() {
    }

    public PermissionDTO(String permCode, String permName, Integer type, Long parentId, String path, Integer sort, Integer status) {
        this.permCode = permCode;
        this.permName = permName;
        this.type = type;
        this.parentId = parentId;
        this.path = path;
        this.sort = sort;
        this.status = status;
    }

    public String permCode() {
        return permCode;
    }

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String permName() {
        return permName;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public Integer type() {
        return type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long parentId() {
        return parentId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String path() {
        return path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer sort() {
        return sort;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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
