package com.demo.molly.util;

import com.demo.molly.vo.MenuVO;
import java.util.Objects;

/**
 * 菜单渲染辅助类，供 Thymeleaf 模板调用。
 * 菜单跳转路径直接使用权限表 path 字段，不再硬编码映射。
 */
public class MenuHelper {

    private final String currentPath;

    public MenuHelper(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getPagePath(String menuPath) {
        return menuPath != null && !menuPath.isEmpty() ? menuPath : "#";
    }

    public boolean isActive(MenuVO menu) {
        return Objects.equals(normalizePath(menu.getPath()), normalizePath(currentPath));
    }

    public boolean hasActiveDescendant(MenuVO menu) {
        if (menu.getChildren() == null || menu.getChildren().isEmpty()) {
            return false;
        }
        for (MenuVO child : menu.getChildren()) {
            if (isActive(child) || hasActiveDescendant(child)) {
                return true;
            }
        }
        return false;
    }

    private static String normalizePath(String path) {
        return path != null ? path.replaceAll("^/", "") : null;
    }
}
