package com.demo.molly.security;

import com.demo.molly.entity.Permission;
import com.demo.molly.entity.Role;
import com.demo.molly.entity.User;
import com.demo.molly.mapper.PermissionMapper;
import com.demo.molly.mapper.RoleMapper;
import com.demo.molly.mapper.UserMapper;
import com.demo.molly.service.TokenCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 加载用户信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public UserDetailsServiceImpl(UserMapper userMapper,
                                  RoleMapper roleMapper,
                                  PermissionMapper permissionMapper,
                                  TokenCacheService tokenCacheService) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<String> roles = tokenCacheService.getUserRoles(user.getId());
        List<String> permissions = tokenCacheService.getUserPermissions(user.getId());

        if (roles == null || permissions == null) {
            List<Role> roleList = roleMapper.selectByUserId(user.getId());
            roles = roleList.stream().map(Role::getRoleCode).collect(Collectors.toList());
            List<Long> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toList());

            permissions = new ArrayList<>();
            if (!roleIds.isEmpty()) {
                List<Permission> perms = permissionMapper.selectByRoleIds(roleIds);
                permissions = perms.stream().map(Permission::getPermCode).distinct().collect(Collectors.toList());
            }

            tokenCacheService.cacheUserRoles(user.getId(), roles);
            tokenCacheService.cacheUserPermissions(user.getId(), permissions);
        }

        return new LoginUser(user, roles, permissions);
    }
}
