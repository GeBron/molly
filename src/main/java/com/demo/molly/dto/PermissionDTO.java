package com.demo.molly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 权限请求参数
 */
public record PermissionDTO(
        @NotBlank(message = "权限编码不能为空")
        @Size(max = 128, message = "权限编码长度不能超过128")
        String permCode,
        @NotBlank(message = "权限名称不能为空")
        @Size(max = 64, message = "权限名称长度不能超过64")
        String permName,
        @NotNull(message = "权限类型不能为空")
        Integer type,
        Long parentId,
        @Size(max = 255, message = "路径长度不能超过255")
        String path,
        Integer sort,
        Integer status
) {
}
