package com.demo.molly.controller;

import com.demo.molly.common.Result;
import com.demo.molly.dto.LoginDTO;
import com.demo.molly.service.AuthService;
import com.demo.molly.vo.LoginResult;
import com.demo.molly.vo.LoginVO;
import com.demo.molly.vo.UserInfoVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Valid LoginDTO dto, HttpServletRequest request, HttpServletResponse response) {
        LoginResult result = authService.login(dto, request);
        setRefreshTokenCookie(response, result.refreshToken(), refreshTokenExpiration);
        return Result.success(new LoginVO(result.accessToken(), "Bearer", result.expiresIn()));
    }

    @PostMapping("/refresh")
    public Result<LoginVO> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        LoginResult result = authService.refresh(refreshToken, request);
        setRefreshTokenCookie(response, result.refreshToken(), refreshTokenExpiration);
        return Result.success(new LoginVO(result.accessToken(), "Bearer", result.expiresIn()));
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request,
                               HttpServletResponse response,
                               @CookieValue(name = "refresh_token", required = false) String refreshToken) {
        String accessToken = extractAccessToken(request);
        authService.logout(accessToken, refreshToken, request);
        clearRefreshTokenCookie(response);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        return Result.success(authService.getUserInfo());
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken, long maxAgeMillis) {
        long maxAgeSeconds = TimeUnit.MILLISECONDS.toSeconds(maxAgeMillis);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(maxAgeSeconds)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
