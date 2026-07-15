package com.demo.molly.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户视图
 */
public record UserVO(
        Long id,
        String username,
        String realName,
        Integer status,
        LocalDateTime createdAt,
        List<Long> roleIds,
        List<String> roleNames
) {
}
