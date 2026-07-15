package com.demo.molly.util;

import com.demo.molly.entity.BaseEntity;

/**
 * 审计字段工具类
 */
public class AuditUtil {

    public static void fillCreate(BaseEntity entity) {
        Long userId = SecurityUtil.getCurrentUserId();
        entity.setCreatedBy(userId != null ? userId : 0L);
        entity.setUpdatedBy(userId != null ? userId : 0L);
        entity.setDeleted(0);
    }

    public static void fillUpdate(BaseEntity entity) {
        Long userId = SecurityUtil.getCurrentUserId();
        entity.setUpdatedBy(userId != null ? userId : 0L);
    }

    public static Long currentUserId() {
        Long userId = SecurityUtil.getCurrentUserId();
        return userId != null ? userId : 0L;
    }
}
