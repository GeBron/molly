package com.demo.molly.vo;

/**
 * 登录结果（包含 Access Token 与 Refresh Token）
 */
public record LoginResult(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
}
