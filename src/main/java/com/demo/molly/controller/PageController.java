package com.demo.molly.controller;

import com.demo.molly.dto.LogQueryDTO;
import com.demo.molly.dto.RoleQueryDTO;
import com.demo.molly.dto.UserQueryDTO;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.service.AuthService;
import com.demo.molly.service.LogService;
import com.demo.molly.service.PermissionService;
import com.demo.molly.service.RoleService;
import com.demo.molly.service.UserService;
import com.demo.molly.util.MenuHelper;
import com.demo.molly.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 页面视图控制器
 */
@Controller
public class PageController {

    private final AuthService authService;
    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final LogService logService;

    @Autowired
    public PageController(AuthService authService,
                          UserService userService,
                          RoleService roleService,
                          PermissionService permissionService,
                          LogService logService) {
        this.authService = authService;
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.logService = logService;
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
        UserQueryDTO query = new UserQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        model.addAttribute("initialData", userService.list(query));
        return "users";
    }

    @PreAuthorize("hasAuthority('role:view')")
    @GetMapping("/roles")
    public String roles(Model model) {
        addLayout(model, "角色管理", "system/role");
        RoleQueryDTO query = new RoleQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        model.addAttribute("initialData", roleService.list(query));
        return "roles";
    }

    @PreAuthorize("hasAuthority('permission:view')")
    @GetMapping("/permissions")
    public String permissions(Model model) {
        addLayout(model, "权限管理", "system/permission");
        model.addAttribute("initialData", permissionService.tree());
        return "permissions";
    }

    @PreAuthorize("hasAuthority('loginLog:view')")
    @GetMapping("/login-logs")
    public String loginLogs(Model model) {
        addLayout(model, "登录日志", "system/login-log");
        LogQueryDTO query = new LogQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        model.addAttribute("initialData", logService.loginLogList(query));
        return "login-logs";
    }

    @PreAuthorize("hasAuthority('operationLog:view')")
    @GetMapping("/operation-logs")
    public String operationLogs(Model model) {
        addLayout(model, "操作日志", "system/operation-log");
        LogQueryDTO query = new LogQueryDTO();
        query.setPageNum(1);
        query.setPageSize(10);
        model.addAttribute("initialData", logService.operationLogList(query));
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
