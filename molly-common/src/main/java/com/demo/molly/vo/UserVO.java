package com.demo.molly.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户视图
 */
public class UserVO {

    private final Long id;
    private final String username;
    private final String realName;
    private final Integer status;
    private final LocalDateTime createdAt;
    private final List<Long> roleIds;
    private final List<String> roleNames;

    public UserVO(Long id, String username, String realName, Integer status, LocalDateTime createdAt,
                  List<Long> roleIds, List<String> roleNames) {
        this.id = id;
        this.username = username;
        this.realName = realName;
        this.status = status;
        this.createdAt = createdAt;
        this.roleIds = roleIds;
        this.roleNames = roleNames;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRealName() {
        return realName;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }
}
