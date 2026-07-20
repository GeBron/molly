package com.demo.molly.service;

import java.util.List;

/**
 * Token 缓存服务接口
 */
public interface TokenCacheService {

    List<String> cacheUserPermissions(Long userId, List<String> permissions);

    List<String> getUserPermissions(Long userId);

    void deleteUserPermissions(Long userId);

    List<String> cacheUserRoles(Long userId, List<String> roles);

    List<String> getUserRoles(Long userId);

    void deleteUserRoles(Long userId);

    void clearUserCache(Long userId);

    void clearAllUserCaches();
}
