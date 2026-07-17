package com.demo.molly.service;

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
import com.demo.molly.security.LoginUser;
import com.demo.molly.security.UserDetailsServiceImpl;
import com.demo.molly.util.IpUtil;
import com.demo.molly.util.SecurityUtil;
import com.demo.molly.vo.MenuVO;
import com.demo.molly.vo.UserInfoVO;
import com.demo.molly.vo.UserVO;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private static final int MAX_LOG_MESSAGE_LENGTH = 255;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final LoginLogMapper loginLogMapper;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager,
                       UserDetailsServiceImpl userDetailsService,
                       UserMapper userMapper,
                       RoleMapper roleMapper,
                       PermissionMapper permissionMapper,
                       LoginLogMapper loginLogMapper,
                       TokenCacheService tokenCacheService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.permissionMapper = permissionMapper;
        this.loginLogMapper = loginLogMapper;
        this.tokenCacheService = tokenCacheService;
    }

    public UserInfoVO login(LoginDTO dto, HttpServletRequest request) {
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null) {
            saveLoginLog(null, dto.getUsername(), "LOGIN", "FAIL", "用户不存在", request);
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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            handleLoginFail(user, request);
            throw new BusinessException(401, "用户名或密码错误");
        } catch (AuthenticationException e) {
            saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "FAIL", e.getMessage(), request);
            throw new BusinessException(401, e.getMessage());
        }

        userMapper.resetLoginFail(user.getId(), user.getId());
        saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "SUCCESS", null, request);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        LoginUser loginUser = (LoginUser) userDetails;
        cacheUserAuthInfo(loginUser);

        return buildUserInfo(loginUser);
    }

    public UserInfoVO getUserInfo() {
        LoginUser loginUser = SecurityUtil.getCurrentUser();
        if (loginUser == null) {
            throw new BusinessException(401, "未登录");
        }
        return buildUserInfo(loginUser);
    }

    private UserInfoVO buildUserInfo(LoginUser loginUser) {
        User user = loginUser.getUser();
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

    private void cacheUserAuthInfo(LoginUser loginUser) {
        tokenCacheService.cacheUserRoles(loginUser.getUser().getId(), loginUser.getRoles());
        tokenCacheService.cacheUserPermissions(loginUser.getUser().getId(), loginUser.getPermissions());
    }

    private boolean isLocked(User user) {
        if (user.getLockTime() == null) {
            return false;
        }
        // 锁定超过 30 分钟自动解锁
        if (user.getLockTime().plusMinutes(30).isBefore(LocalDateTime.now())) {
            userMapper.resetLoginFail(user.getId(), 0L);
            return false;
        }
        return true;
    }

    private void handleLoginFail(User user, HttpServletRequest request) {
        int count = user.getLoginFailCount() == null ? 0 : user.getLoginFailCount();
        count++;
        LocalDateTime lockTime = null;
        if (count >= 5) {
            lockTime = LocalDateTime.now();
        }
        userMapper.updateLoginFail(user.getId(), count, lockTime, user.getId());
        saveLoginLog(user.getId(), user.getUsername(), "LOGIN", "FAIL", "密码错误", request);
    }

    private void saveLoginLog(Long userId, String username, String operation, String status, String message, HttpServletRequest request) {
        try {
            LoginLog log = new LoginLog();
            log.setUserId(userId);
            log.setUsername(username);
            log.setOperation(operation);
            log.setStatus(status);
            if (message != null && message.length() > MAX_LOG_MESSAGE_LENGTH) {
                message = message.substring(0, MAX_LOG_MESSAGE_LENGTH);
            }
            log.setMessage(message);
            log.setIp(IpUtil.getIp(request));
            Long operatorId = userId != null ? userId : 0L;
            log.setCreatedBy(operatorId);
            log.setUpdatedBy(operatorId);
            loginLogMapper.insert(log);
        } catch (Exception e) {
            logger.warn("保存登录日志失败，username={}, operation={}, status={}", username, operation, status, e);
        }
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
