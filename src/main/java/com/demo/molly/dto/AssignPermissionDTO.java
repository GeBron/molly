package com.demo.molly.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 分配权限请求参数
 */
public record AssignPermissionDTO(
        @NotNull(message = "权限ID列表不能为空")
        List<Long> permissionIds
) {
}
