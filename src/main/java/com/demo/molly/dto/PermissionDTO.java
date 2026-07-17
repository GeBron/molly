package com.demo.molly.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Min(value = 1, message = "权限类型必须为1-4")
    @Max(value = 4, message = "权限类型必须为1-4")
    private Integer type;

    @NotNull(message = "父级ID不能为空")
    @Min(value = 0, message = "父级ID不能为负数")
    private Long parentId;

    @Size(max = 255, message = "路径长度不能超过255")
    private String path;

    @NotNull(message = "排序不能为空")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
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

    public String getPermCode() {
        return permCode;
    }

    public void setPermCode(String permCode) {
        this.permCode = permCode;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
