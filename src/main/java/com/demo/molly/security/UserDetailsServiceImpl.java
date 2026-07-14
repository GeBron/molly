package com.demo.molly.security;

import com.demo.molly.entity.Permission;
import com.demo.molly.entity.Role;
import com.demo.molly.entity.User;
import com.demo.molly.mapper.PermissionMapper;
import com.demo.molly.mapper.RoleMapper;
import com.demo.molly.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载用户信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @Autowired
    public UserDetailsServiceImpl(UserMapper userMapper, RoleMapper roleMapper, PermissionMapper permissionMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        List<Role> roles = roleMapper.selectByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(Role::getRoleCode).toList();
        List<Long> roleIds = roles.stream().map(Role::getId).toList();

        List<String> permissions = new ArrayList<>();
        if (!roleIds.isEmpty()) {
            List<Permission> perms = permissionMapper.selectByRoleIds(roleIds);
            permissions = perms.stream().map(Permission::getPermCode).distinct().toList();
        }

        return new LoginUser(user, roleCodes, permissions);
    }
}
