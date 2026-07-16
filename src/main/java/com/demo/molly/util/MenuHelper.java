package com.demo.molly.util;

import com.demo.molly.vo.MenuVO;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 菜单渲染辅助类，供 Thymeleaf 模板调用
 */
public class MenuHelper {

    private static final Map<String, String> PATH_TO_PAGE = new HashMap<>();

    static {
        PATH_TO_PAGE.put("dashboard", "/dashboard");
        PATH_TO_PAGE.put("system/user", "/users");
        PATH_TO_PAGE.put("system/role", "/roles");
        PATH_TO_PAGE.put("system/permission", "/permissions");
        PATH_TO_PAGE.put("system/login-log", "/login-logs");
        PATH_TO_PAGE.put("system/operation-log", "/operation-logs");
    }

    private final String currentPath;

    public MenuHelper(String currentPath) {
        this.currentPath = currentPath;
    }

    public String getPagePath(String menuPath) {
        String key = menuPath != null ? menuPath.replaceAll("^/", "") : null;
        String path = key != null ? PATH_TO_PAGE.get(key) : null;
        if (path != null) {
            return path;
        }
        return key != null && !key.isEmpty() ? "/" + key + ".html" : "#";
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
