package com.demo.molly.vo;

import java.util.List;

/**
 * 当前用户信息响应
 */
public class UserInfoVO {

    private final UserVO user;
    private final List<String> roles;
    private final List<String> permissions;
    private final List<MenuVO> menus;

    public UserInfoVO(UserVO user, List<String> roles, List<String> permissions, List<MenuVO> menus) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
        this.menus = menus;
    }

    public UserVO getUser() {
        return user;
    }

    public List<String> getRoles() {
        return roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<MenuVO> getMenus() {
        return menus;
    }
}
