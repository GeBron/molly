package com.demo.molly.vo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限视图
 */
public class PermissionVO {

    private final Long id;
    private final String permCode;
    private final String permName;
    private final Integer type;
    private final Long parentId;
    private final String path;
    private final Integer sort;
    private final Integer status;
    private final LocalDateTime createdAt;
    private final List<PermissionVO> children;

    public PermissionVO(Long id, String permCode, String permName, Integer type, Long parentId,
                        String path, Integer sort, Integer status, LocalDateTime createdAt, List<PermissionVO> children) {
        this.id = id;
        this.permCode = permCode;
        this.permName = permName;
        this.type = type;
        this.parentId = parentId;
        this.path = path;
        this.sort = sort;
        this.status = status;
        this.createdAt = createdAt;
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public String getPermCode() {
        return permCode;
    }

    public String getPermName() {
        return permName;
    }

    public Integer getType() {
        return type;
    }

    public Long getParentId() {
        return parentId;
    }

    public String getPath() {
        return path;
    }

    public Integer getSort() {
        return sort;
    }

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<PermissionVO> getChildren() {
        return children;
    }
}
