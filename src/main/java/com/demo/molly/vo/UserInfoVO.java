package com.demo.molly.vo;

import java.util.List;

/**
 * 当前用户信息响应
 */
public record UserInfoVO(
        UserVO user,
        List<String> roles,
        List<String> permissions,
        List<MenuVO> menus
) {
}
