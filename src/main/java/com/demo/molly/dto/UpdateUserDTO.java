package com.demo.molly.dto;

import jakarta.validation.constraints.Size;

/**
 * 修改用户请求参数
 */
public record UpdateUserDTO(
        @Size(max = 64, message = "真实姓名长度不能超过64")
        String realName,
        Integer status
) {
}
