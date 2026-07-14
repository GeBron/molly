package com.demo.molly.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限视图
 */
public record PermissionVO(
        Long id,
        String permCode,
        String permName,
        Integer type,
        Long parentId,
        String path,
        Integer sort,
        Integer status,
        LocalDateTime createdAt,
        List<PermissionVO> children
) {
}
