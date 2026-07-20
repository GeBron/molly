package com.demo.molly.service.impl;

import com.demo.molly.entity.Permission;
import com.demo.molly.entity.Role;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.PermissionMapper;
import com.demo.molly.mapper.RoleMapper;
import com.demo.molly.security.LoginUser;
import com.demo.molly.service.AuthService;
import com.demo.molly.util.SecurityUtil;
import com.demo.molly.vo.MenuVO;
import com.demo.molly.vo.UserInfoVO;
import com.demo.molly.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 认证服务
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public AuthServiceImpl(RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    @Cacheable(value = "user:info", key = "T(com.demo.molly.util.SecurityUtil).getCurrentUserId()")
    public UserInfoVO getUserInfo() {
        LoginUser loginUser = SecurityUtil.getCurrentUser();
        if (loginUser == null) {
            throw new BusinessException(401, "未登录");
        }
        return buildUserInfo(loginUser);
    }

    private UserInfoVO buildUserInfo(LoginUser loginUser) {
        var user = loginUser.getUser();
        UserVO userVO = new UserVO(user.getId(), user.getUsername(), user.getRealName(), user.getStatus(), user.getCreatedAt(), null, null);

        List<Role> roles = roleMapper.selectByUserId(user.getId());
        List<String> roleCodes = new ArrayList<>();
        List<Long> roleIds = new ArrayList<>();
        for (Role role : roles) {
            roleCodes.add(role.getRoleCode());
            roleIds.add(role.getId());
        }

        List<String> permissions = loginUser.getPermissions();
        List<MenuVO> menus = buildMenus(roleIds);

        return new UserInfoVO(userVO, roleCodes, permissions, menus);
    }

    private List<MenuVO> buildMenus(List<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Permission> permissions = permissionMapper.selectByRoleIds(roleIds);
        List<Permission> menuPermissions = new ArrayList<>();
        for (Permission p : permissions) {
            if (p.getType() == 1 || p.getType() == 2) {
                menuPermissions.add(p);
            }
        }
        Collections.sort(menuPermissions, new Comparator<Permission>() {
            @Override
            public int compare(Permission o1, Permission o2) {
                Integer s1 = o1.getSort() == null ? 0 : o1.getSort();
                Integer s2 = o2.getSort() == null ? 0 : o2.getSort();
                return s1.compareTo(s2);
            }
        });

        Map<Long, MenuVO> map = new HashMap<>();
        for (Permission p : menuPermissions) {
            map.put(p.getId(), new MenuVO(p.getId(), p.getPermName(), p.getPath(), p.getType(), p.getPermCode(), null, new ArrayList<>()));
        }

        List<MenuVO> roots = new ArrayList<>();
        for (Permission p : menuPermissions) {
            MenuVO menu = map.get(p.getId());
            if (p.getParentId() == null || p.getParentId() == 0L) {
                roots.add(menu);
            } else {
                MenuVO parent = map.get(p.getParentId());
                if (parent != null && parent.getChildren() != null) {
                    parent.getChildren().add(menu);
                }
            }
        }
        return roots;
    }
}
