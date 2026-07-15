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

    public Long id() {
        return id;
    }

    public Long getId() {
        return id;
    }

    public String name() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String path() {
        return path;
    }

    public String getPath() {
        return path;
    }

    public Integer type() {
        return type;
    }

    public Integer getType() {
        return type;
    }

    public String permCode() {
        return permCode;
    }

    public String getPermCode() {
        return permCode;
    }

    public String icon() {
        return icon;
    }

    public String getIcon() {
        return icon;
    }

    public List<MenuVO> children() {
        return children;
    }

    public List<MenuVO> getChildren() {
        return children;
    }
}
