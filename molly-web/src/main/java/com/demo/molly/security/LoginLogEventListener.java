package com.demo.molly.security;

import com.demo.molly.entity.LoginLog;
import com.demo.molly.entity.User;
import com.demo.molly.mapper.LoginLogMapper;
import com.demo.molly.mapper.UserMapper;
import com.demo.molly.service.TokenCacheService;
import com.demo.molly.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * 登录日志事件监听器：处理登录成功/失败、账户锁定、缓存认证信息
 */
@Component
public class LoginLogEventListener {

    private static final Logger logger = LoggerFactory.getLogger(LoginLogEventListener.class);

    private static final int MAX_LOG_MESSAGE_LENGTH = 255;
    private static final int MAX_LOGIN_FAIL_COUNT = 5;
    private static final long LOCK_MINUTES = 30;

    private final UserMapper userMapper;
    private final LoginLogMapper loginLogMapper;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public LoginLogEventListener(UserMapper userMapper,
                                 LoginLogMapper loginLogMapper,
                                 TokenCacheService tokenCacheService) {
        this.userMapper = userMapper;
        this.loginLogMapper = loginLogMapper;
        this.tokenCacheService = tokenCacheService;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof LoginUser loginUser)) {
            return;
        }

        Long userId = loginUser.getUser().getId();
        String username = loginUser.getUsername();

        userMapper.resetLoginFail(userId, userId);
        cacheUserAuthInfo(loginUser);
        saveLoginLog(userId, username, "LOGIN", "SUCCESS", null);
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        Authentication authentication = event.getAuthentication();
        String username = extractUsername(authentication);
        if (username == null) {
            return;
        }

        User user = userMapper.findByUsername(username);
        if (user == null) {
            saveLoginLog(null, username, "LOGIN", "FAIL", "用户不存在");
            return;
        }

        if (isLocked(user)) {
            saveLoginLog(user.getId(), username, "LOGIN", "FAIL", "账号已锁定");
            return;
        }

        int count = user.getLoginFailCount() == null ? 0 : user.getLoginFailCount();
        count++;
        LocalDateTime lockTime = null;
        if (count >= MAX_LOGIN_FAIL_COUNT) {
            lockTime = LocalDateTime.now();
        }
        userMapper.updateLoginFail(user.getId(), count, lockTime, user.getId());

        String message = event.getException() != null ? event.getException().getMessage() : "认证失败";
        if (message == null || message.isEmpty()) {
            message = "用户名或密码错误";
        }
        saveLoginLog(user.getId(), username, "LOGIN", "FAIL", message);
    }

    private void cacheUserAuthInfo(LoginUser loginUser) {
        tokenCacheService.cacheUserRoles(loginUser.getUser().getId(), loginUser.getRoles());
        tokenCacheService.cacheUserPermissions(loginUser.getUser().getId(), loginUser.getPermissions());
    }

    private boolean isLocked(User user) {
        if (user.getLockTime() == null) {
            return false;
        }
        if (user.getLockTime().plusMinutes(LOCK_MINUTES).isBefore(LocalDateTime.now())) {
            userMapper.resetLoginFail(user.getId(), 0L);
            return false;
        }
        return true;
    }

    private String extractUsername(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String s) {
            return s;
        }
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (authentication instanceof AbstractAuthenticationToken token && token.getName() != null) {
            return token.getName();
        }
        return null;
    }

    private void saveLoginLog(Long userId, String username, String operation, String status, String message) {
        try {
            var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            LoginLog log = new LoginLog();
            log.setUserId(userId);
            log.setUsername(username);
            log.setOperation(operation);
            log.setStatus(status);
            if (message != null && message.length() > MAX_LOG_MESSAGE_LENGTH) {
                message = message.substring(0, MAX_LOG_MESSAGE_LENGTH);
            }
            log.setMessage(message);
            if (attrs != null) {
                log.setIp(IpUtil.getIp(attrs.getRequest()));
            }
            Long operatorId = userId != null ? userId : 0L;
            log.setCreatedBy(operatorId);
            log.setUpdatedBy(operatorId);
            loginLogMapper.insert(log);
        } catch (Exception e) {
            logger.warn("保存登录日志失败，username={}, operation={}, status={}", username, operation, status, e);
        }
    }
}
