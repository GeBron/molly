package com.demo.molly.dto;

/**
 * 角色查询参数
 */
public record RoleQueryDTO(
        String roleName,
        Integer status,
        Integer pageNum,
        Integer pageSize
) {
}
