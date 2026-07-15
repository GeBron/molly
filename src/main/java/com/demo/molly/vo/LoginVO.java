package com.demo.molly.vo;

/**
 * 登录响应
 */
public record LoginVO(
        String token,
        String tokenType,
        long expiresIn
) {
}
