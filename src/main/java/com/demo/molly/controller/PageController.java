package com.demo.molly.controller;

import com.demo.molly.common.PageResult;
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
import com.demo.molly.vo.LoginLogVO;
import com.demo.molly.vo.OperationLogVO;
import com.demo.molly.vo.PermissionVO;
import com.demo.molly.vo.RoleVO;
import com.demo.molly.vo.UserInfoVO;
import com.demo.molly.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 页面视图控制器：负责页面路由、布局数据以及首屏业务数据渲染
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

    @PreAuthorize("hasAuthority('user:view')")
    @GetMapping("/users")
    public String users(Model model) {
        addLayout(model, "用户管理", "system/user");
        PageResult<UserVO> initialData = userService.list(new UserQueryDTO(null, null, 1, 10));
        model.addAttribute("initialData", initialData);
        return "users";
    }

    @PreAuthorize("hasAuthority('role:view')")
    @GetMapping("/roles")
    public String roles(Model model) {
        addLayout(model, "角色管理", "system/role");
        PageResult<RoleVO> initialData = roleService.list(new RoleQueryDTO(null, null, 1, 10));
        model.addAttribute("initialData", initialData);
        return "roles";
    }

    @PreAuthorize("hasAuthority('permission:view')")
    @GetMapping("/permissions")
    public String permissions(Model model) {
        addLayout(model, "权限管理", "system/permission");
        List<PermissionVO> initialData = permissionService.tree();
        model.addAttribute("initialData", initialData);
        return "permissions";
    }

    @PreAuthorize("hasAuthority('loginLog:view')")
    @GetMapping("/login-logs")
    public String loginLogs(Model model) {
        addLayout(model, "登录日志", "system/login-log");
        PageResult<LoginLogVO> initialData = logService.loginLogList(new LogQueryDTO(1, 10, null, null));
        model.addAttribute("initialData", initialData);
        return "login-logs";
    }

    @PreAuthorize("hasAuthority('operationLog:view')")
    @GetMapping("/operation-logs")
    public String operationLogs(Model model) {
        addLayout(model, "操作日志", "system/operation-log");
        PageResult<OperationLogVO> initialData = logService.operationLogList(new LogQueryDTO(1, 10, null, null));
        model.addAttribute("initialData", initialData);
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
