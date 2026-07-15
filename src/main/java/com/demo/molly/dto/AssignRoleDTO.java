package com.demo.molly.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 分配角色请求参数
 */
public record AssignRoleDTO(
        @NotNull(message = "角色ID列表不能为空")
        List<Long> roleIds
) {
}
