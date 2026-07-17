package com.demo.molly.controller;

import com.demo.molly.exception.BusinessException;
import com.demo.molly.service.AuthService;
import com.demo.molly.util.MenuHelper;
import com.demo.molly.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面视图控制器：仅负责页面路由与布局数据，业务数据由前端通过 REST 接口获取
 */
@Controller
public class PageController {

    private final AuthService authService;

    @Autowired
    public PageController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String root(Model model) {
        try {
            addLayout(model, "仪表盘", "dashboard");
            return "dashboard";
        } catch (BusinessException e) {
            if (e.getCode() == 401) {
                return "redirect:/login";
            }
            throw e;
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        addLayout(model, "仪表盘", "dashboard");
        return "dashboard";
    }

    @PreAuthorize("hasAuthority('user:view')")
    @GetMapping("/users")
    public String users(Model model) {
        addLayout(model, "用户管理", "system/user");
        return "users";
    }

    @PreAuthorize("hasAuthority('role:view')")
    @GetMapping("/roles")
    public String roles(Model model) {
        addLayout(model, "角色管理", "system/role");
        return "roles";
    }

    @PreAuthorize("hasAuthority('permission:view')")
    @GetMapping("/permissions")
    public String permissions(Model model) {
        addLayout(model, "权限管理", "system/permission");
        return "permissions";
    }

    @PreAuthorize("hasAuthority('loginLog:view')")
    @GetMapping("/login-logs")
    public String loginLogs(Model model) {
        addLayout(model, "登录日志", "system/login-log");
        return "login-logs";
    }

    @PreAuthorize("hasAuthority('operationLog:view')")
    @GetMapping("/operation-logs")
    public String operationLogs(Model model) {
        addLayout(model, "操作日志", "system/operation-log");
        return "operation-logs";
    }

    private void addLayout(Model model, String pageTitle, String currentPath) {
        UserInfoVO userInfo = authService.getUserInfo();
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("menus", userInfo.getMenus());
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("currentPath", currentPath);
        model.addAttribute("menuHelper", new MenuHelper(currentPath));
    }
}
