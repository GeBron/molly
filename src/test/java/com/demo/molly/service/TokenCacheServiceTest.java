package com.demo.molly.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")
class TokenCacheServiceTest {

    @Autowired
    private TokenCacheService tokenCacheService;

    @Test
    void testCacheUserPermissions() {
        Long userId = 1L;
        List<String> permissions = Arrays.asList("user:view", "user:create");

        tokenCacheService.cacheUserPermissions(userId, permissions);
        List<String> cached = tokenCacheService.getUserPermissions(userId);

        assertEquals(permissions, cached);

        tokenCacheService.deleteUserPermissions(userId);
        assertNull(tokenCacheService.getUserPermissions(userId));
    }

    @Test
    void testCacheUserRoles() {
        Long userId = 2L;
        List<String> roles = Arrays.asList("admin", "user");

        tokenCacheService.cacheUserRoles(userId, roles);
        List<String> cached = tokenCacheService.getUserRoles(userId);

        assertEquals(roles, cached);

        tokenCacheService.clearUserCache(userId);
        assertNull(tokenCacheService.getUserRoles(userId));
        assertNull(tokenCacheService.getUserPermissions(userId));
    }

    @Test
    void testClearUserCache() {
        Long userId = 3L;
        List<String> permissions = Arrays.asList("role:view");
        List<String> roles = Arrays.asList("operator");

        tokenCacheService.cacheUserPermissions(userId, permissions);
        tokenCacheService.cacheUserRoles(userId, roles);

        assertEquals(permissions, tokenCacheService.getUserPermissions(userId));
        assertEquals(roles, tokenCacheService.getUserRoles(userId));

        tokenCacheService.clearUserCache(userId);

        assertNull(tokenCacheService.getUserPermissions(userId));
        assertNull(tokenCacheService.getUserRoles(userId));
    }
}
