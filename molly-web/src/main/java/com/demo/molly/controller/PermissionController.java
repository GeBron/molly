package com.demo.molly.controller;

import com.demo.molly.aspect.OperationLog;
import com.demo.molly.common.Result;
import com.demo.molly.dto.PermissionDTO;
import com.demo.molly.service.PermissionService;
import com.demo.molly.vo.PermissionVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理控制器
 */
@Validated
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasAuthority('permission:view')")
    @GetMapping
    public Result<List<PermissionVO>> tree() {
        return Result.success(permissionService.tree());
    }

    @PreAuthorize("hasAuthority('permission:view')")
    @GetMapping("/{id}")
    public Result<PermissionVO> detail(@PathVariable Long id) {
        return Result.success(permissionService.detail(id));
    }

    @OperationLog(module = "权限管理", operation = "新增权限")
    @PreAuthorize("hasAuthority('permission:create')")
    @PostMapping
    public Result<Void> create(@RequestBody @Valid PermissionDTO dto) {
        permissionService.create(dto);
        return Result.success();
    }

    @OperationLog(module = "权限管理", operation = "修改权限")
    @PreAuthorize("hasAuthority('permission:update')")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid PermissionDTO dto) {
        permissionService.update(id, dto);
        return Result.success();
    }

    @OperationLog(module = "权限管理", operation = "删除权限")
    @PreAuthorize("hasAuthority('permission:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        permissionService.delete(id);
        return Result.success();
    }

    @OperationLog(module = "权限管理", operation = "修改权限状态")
    @PreAuthorize("hasAuthority('permission:update')")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                     @RequestParam @Min(value = 0, message = "状态值不合法") @Max(value = 1, message = "状态值不合法") Integer status) {
        permissionService.updateStatus(id, status);
        return Result.success();
    }
}
