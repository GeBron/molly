package com.demo.molly.service;

import com.demo.molly.common.Result;
import com.demo.molly.dto.LoginDTO;
import com.demo.molly.entity.LoginLog;
import com.demo.molly.entity.Permission;
import com.demo.molly.entity.Role;
import com.demo.molly.entity.User;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.LoginLogMapper;
import com.demo.molly.mapper.PermissionMapper;
import com.demo.molly.mapper.RoleMapper;
import com.demo.molly.mapper.UserMapper;
import com.demo.molly.security.JwtUtil;
import com.demo.molly.security.LoginUser;
import com.demo.molly.security.UserDetailsServiceImpl;
import com.demo.molly.util.IpUtil;
import com.demo.molly.util.SecurityUtil;
import com.demo.molly.vo.LoginVO;
import com.demo.molly.vo.MenuVO;
import com.demo.molly.vo.UserInfoVO;
import com.demo.molly.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证服务
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final LoginLogMapper loginLogMapper;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserDetailsServiceImpl userDetailsService,
                       JwtUtil jwtUtil,
                       UserMapper userMapper,
                       RoleMapper roleMapper,
                       PermissionMapper permissionMapper,
                       LoginLogMapper loginLogMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.loginLogMapper = loginLogMapper;
    }

    public LoginVO login(LoginDTO dto, HttpServletRequest request) {
        User user = userMapper.findByUsername(dto.username());
        if (user == null) {
            saveLoginLog(null, dto.username(), "LOGIN", "FAIL", "用户不存在", request);
            throw new BusinessException(401, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "FAIL", "账号已禁用", request);
            throw new BusinessException(401, "账号已禁用");
        }
        if (isLocked(user)) {
            saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "FAIL", "账号已锁定", request);
            throw new BusinessException(401, "账号已锁定，请30分钟后重试");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        } catch (BadCredentialsException e) {
            handleLoginFail(user, request);
            throw new BusinessException(401, "用户名或密码错误");
        } catch (AuthenticationException e) {
            saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "FAIL", e.getMessage(), request);
            throw new BusinessException(401, e.getMessage());
        }

        userMapper.resetLoginFail(user.getId());
        saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "SUCCESS", null, request);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        return new LoginVO(token, "Bearer", 86400L);
    }

    public void logout(HttpServletRequest request) {
        LoginUser loginUser = SecurityUtil.getCurrentUser();
        if (loginUser != null) {
            saveLoginLog(loginUser.getUser().getId(), loginUser.getUsername(), "LOGOUT", "SUCCESS", null, request);
        }
    }

    public UserInfoVO getUserInfo() {
        LoginUser loginUser = SecurityUtil.getCurrentUser();
        if (loginUser == null) {
            throw new BusinessException(401, "未登录");
        }
        User user = loginUser.getUser();
        UserVO userVO = new UserVO(user.getId(), user.getUsername(), user.getRealName(), user.getStatus(), user.getCreatedAt(), null, null);

        List<Role> roles = roleMapper.selectByUserId(user.getId());
        List<String> roleCodes = roles.stream().map(Role::getRoleCode).toList();
        List<Long> roleIds = roles.stream().map(Role::getId).toList();

        List<String> permissions = loginUser.getPermissions();
        List<MenuVO> menus = buildMenus(roleIds);

        return new UserInfoVO(userVO, roleCodes, permissions, menus);
    }

    private boolean isLocked(User user) {
        if (user.getLockTime() == null) {
            return false;
        }
        return !user.getLockTime().plusMinutes(30).isBefore(LocalDateTime.now());
    }

    private void handleLoginFail(User user, HttpServletRequest request) {
        int count = user.getLoginFailCount() == null ? 0 : user.getLoginFailCount();
        count++;
        LocalDateTime lockTime = null;
        if (count >= 5) {
            lockTime = LocalDateTime.now();
        }
        userMapper.updateLoginFail(user.getId(), count, lockTime);
        saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "FAIL", "密码错误", request);
    }

    private void saveLoginLog(Long userId, String username, String operation, String status, String message, HttpServletRequest request) {
        LoginLog log = new LoginLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setOperation(operation);
        log.setStatus(status);
        log.setMessage(message);
        log.setIp(IpUtil.getIp(request));
        loginLogMapper.insert(log);
    }

    private List<MenuVO> buildMenus(List<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Permission> permissions = permissionMapper.selectByRoleIds(roleIds);
        List<Permission> menuPermissions = permissions.stream()
                .filter(p -> p.getType() == 1 || p.getType() == 2)
                .sorted(Comparator.comparingInt(p -> p.getSort() == null ? 0 : p.getSort()))
                .toList();

        Map<Long, MenuVO> map = menuPermissions.stream()
                .collect(Collectors.toMap(Permission::getId, p -> new MenuVO(p.getId(), p.getPermName(), p.getPath(), p.getType(), new ArrayList<>())));

        List<MenuVO> roots = new ArrayList<>();
        for (Permission p : menuPermissions) {
            MenuVO menu = map.get(p.getId());
            if (p.getParentId() == null || p.getParentId() == 0L) {
                roots.add(menu);
            } else {
                MenuVO parent = map.get(p.getParentId());
                if (parent != null && parent.children() != null) {
                    parent.children().add(menu);
                }
            }
        }
        return roots;
    }
}
