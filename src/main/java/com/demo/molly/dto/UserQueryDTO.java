package com.demo.molly.dto;

/**
 * 用户查询参数
 */
public record UserQueryDTO(
        String username,
        Integer status,
        Integer pageNum,
        Integer pageSize
) {
}
