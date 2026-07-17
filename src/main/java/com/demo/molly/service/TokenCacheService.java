package com.demo.molly.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Token 缓存服务：缓存用户权限、角色等信息
 */
@Service
public class TokenCacheService {

    private static final String USER_PERMISSIONS_CACHE = "user:permissions";
    private static final String USER_ROLES_CACHE = "user:roles";

    @CachePut(value = USER_PERMISSIONS_CACHE, key = "#userId")
    public List<String> cacheUserPermissions(Long userId, List<String> permissions) {
        return permissions;
    }

    @Cacheable(value = USER_PERMISSIONS_CACHE, key = "#userId", unless = "#result == null")
    public List<String> getUserPermissions(Long userId) {
        return null;
    }

    @CacheEvict(value = USER_PERMISSIONS_CACHE, key = "#userId")
    public void deleteUserPermissions(Long userId) {
    }

    @CachePut(value = USER_ROLES_CACHE, key = "#userId")
    public List<String> cacheUserRoles(Long userId, List<String> roles) {
        return roles;
    }

    @Cacheable(value = USER_ROLES_CACHE, key = "#userId", unless = "#result == null")
    public List<String> getUserRoles(Long userId) {
        return null;
    }

    @CacheEvict(value = USER_ROLES_CACHE, key = "#userId")
    public void deleteUserRoles(Long userId) {
    }

    @Caching(evict = {
            @CacheEvict(value = USER_PERMISSIONS_CACHE, key = "#userId"),
            @CacheEvict(value = USER_ROLES_CACHE, key = "#userId")
    })
    public void clearUserCache(Long userId) {
    }
}
