package com.demo.molly.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用户请求参数
 */
public record UserDTO(
        @NotBlank(message = "用户名不能为空")
        @Size(max = 64, message = "用户名长度不能超过64")
        String username,
        @NotBlank(message = "密码不能为空")
        @Size(min = 6, message = "密码长度不能小于6")
        String password,
        @Size(max = 64, message = "真实姓名长度不能超过64")
        String realName,
        Integer status
) {
}
