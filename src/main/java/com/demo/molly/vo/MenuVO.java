package com.demo.molly.vo;

import java.util.List;

/**
 * 菜单视图
 */
public class MenuVO {

    private final Long id;
    private final String name;
    private final String path;
    private final Integer type;
    private final String permCode;
    private final String icon;
    private final List<MenuVO> children;

    public MenuVO(Long id, String name, String path, Integer type, String permCode, String icon, List<MenuVO> children) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.permCode = permCode;
        this.icon = icon;
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Integer getType() {
        return type;
    }

    public String getPermCode() {
        return permCode;
    }

    public String getIcon() {
        return icon;
    }

    public List<MenuVO> getChildren() {
        return children;
    }
}
