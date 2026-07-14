package com.demo.molly.controller;

import com.demo.molly.aspect.OperationLog;
import com.demo.molly.common.PageResult;
import com.demo.molly.common.Result;
import com.demo.molly.dto.AssignPermissionDTO;
import com.demo.molly.dto.RoleDTO;
import com.demo.molly.dto.RoleQueryDTO;
import com.demo.molly.service.RoleService;
import com.demo.molly.vo.RoleVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasAuthority('role:view')")
    @GetMapping
    public Result<PageResult<RoleVO>> list(RoleQueryDTO query) {
        return Result.success(roleService.list(query));
    }

    @PreAuthorize("hasAuthority('role:view')")
    @GetMapping("/{id}")
    public Result<RoleVO> detail(@PathVariable Long id) {
        return Result.success(roleService.detail(id));
    }

    @OperationLog(module = "角色管理", operation = "新增角色")
    @PreAuthorize("hasAuthority('role:create')")
    @PostMapping
    public Result<Void> create(@RequestBody @Valid RoleDTO dto) {
        roleService.create(dto);
        return Result.success();
    }

    @OperationLog(module = "角色管理", operation = "修改角色")
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid RoleDTO dto) {
        roleService.update(id, dto);
        return Result.success();
    }

    @OperationLog(module = "角色管理", operation = "删除角色")
    @PreAuthorize("hasAuthority('role:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @OperationLog(module = "角色管理", operation = "修改角色状态")
    @PreAuthorize("hasAuthority('role:update')")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, Integer status) {
        roleService.updateStatus(id, status);
        return Result.success();
    }

    @OperationLog(module = "角色管理", operation = "分配权限")
    @PreAuthorize("hasAuthority('role:assign-perm')")
    @PostMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable Long id, @RequestBody @Valid AssignPermissionDTO dto) {
        roleService.assignPermissions(id, dto);
        return Result.success();
    }
}
