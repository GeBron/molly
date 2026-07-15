package com.demo.molly.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Token 缓存服务：缓存用户权限、角色等信息
 */
@Service
public class TokenCacheService {

    private static final String USER_PERMISSIONS_PREFIX = "user:permissions:";
    private static final String USER_ROLES_PREFIX = "user:roles:";

    @Value("${session.cache-timeout:1800}")
    private long cacheTimeoutSeconds;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public TokenCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void cacheUserPermissions(Long userId, List<String> permissions) {
        cacheUserPermissions(userId, permissions, cacheTimeoutSeconds);
    }

    public void cacheUserPermissions(Long userId, List<String> permissions, long expirationSeconds) {
        try {
            String value = objectMapper.writeValueAsString(permissions);
            redisTemplate.opsForValue().set(USER_PERMISSIONS_PREFIX + userId, value, expirationSeconds, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("缓存用户权限失败", e);
        }
    }

    public List<String> getUserPermissions(Long userId) {
        String value = redisTemplate.opsForValue().get(USER_PERMISSIONS_PREFIX + userId);
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(value, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("读取用户权限缓存失败", e);
        }
    }

    public void deleteUserPermissions(Long userId) {
        redisTemplate.delete(USER_PERMISSIONS_PREFIX + userId);
    }

    public void cacheUserRoles(Long userId, List<String> roles) {
        cacheUserRoles(userId, roles, cacheTimeoutSeconds);
    }

    public void cacheUserRoles(Long userId, List<String> roles, long expirationSeconds) {
        try {
            String value = objectMapper.writeValueAsString(roles);
            redisTemplate.opsForValue().set(USER_ROLES_PREFIX + userId, value, expirationSeconds, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("缓存用户角色失败", e);
        }
    }

    public List<String> getUserRoles(Long userId) {
        String value = redisTemplate.opsForValue().get(USER_ROLES_PREFIX + userId);
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.readValue(value, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("读取用户角色缓存失败", e);
        }
    }

    public void deleteUserRoles(Long userId) {
        redisTemplate.delete(USER_ROLES_PREFIX + userId);
    }

    public void clearUserCache(Long userId) {
        deleteUserPermissions(userId);
        deleteUserRoles(userId);
    }
}
