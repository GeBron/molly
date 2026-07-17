package com.demo.molly.controller;

import com.demo.molly.common.Result;
import com.demo.molly.dto.LoginDTO;
import com.demo.molly.service.AuthService;
import com.demo.molly.vo.UserInfoVO;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<UserInfoVO> login(@RequestBody @Valid LoginDTO dto, HttpServletRequest request) {
        return Result.success(authService.login(dto, request));
    }

    @GetMapping("/info")
    public Result<UserInfoVO> info() {
        return Result.success(authService.getUserInfo());
    }
}
