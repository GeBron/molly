package com.demo.molly.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色视图
 */
public record RoleVO(
        Long id,
        String roleCode,
        String roleName,
        Integer status,
        LocalDateTime createdAt,
        List<Long> permissionIds
) {
}
