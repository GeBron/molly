package com.demo.molly.controller;

import com.demo.molly.aspect.OperationLog;
import com.demo.molly.common.PageResult;
import com.demo.molly.common.Result;
import com.demo.molly.dto.AssignIdsDTO;
import com.demo.molly.dto.KeywordStatusQuery;
import com.demo.molly.dto.UpdateUserDTO;
import com.demo.molly.dto.UserDTO;
import com.demo.molly.service.UserService;
import com.demo.molly.vo.UserVO;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

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

/**
 * 用户管理控制器
 */
@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('user:view')")
    @GetMapping
    public Result<PageResult<UserVO>> list(KeywordStatusQuery query) {
        return Result.success(userService.list(query));
    }

    @PreAuthorize("hasAuthority('user:view')")
    @GetMapping("/{id}")
    public Result<UserVO> detail(@PathVariable Long id) {
        return Result.success(userService.detail(id));
    }

    @OperationLog(module = "用户管理", operation = "新增用户")
    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping
    public Result<Void> create(@RequestBody @Valid UserDTO dto) {
        userService.create(dto);
        return Result.success();
    }

    @OperationLog(module = "用户管理", operation = "修改用户")
    @PreAuthorize("hasAuthority('user:update')")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO dto) {
        userService.update(id, dto);
        return Result.success();
    }

    @OperationLog(module = "用户管理", operation = "删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }

    @OperationLog(module = "用户管理", operation = "修改用户状态")
    @PreAuthorize("hasAuthority('user:update')")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id,
                                     @RequestParam @Min(value = 0, message = "状态值不合法") @Max(value = 1, message = "状态值不合法") Integer status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @OperationLog(module = "用户管理", operation = "分配角色")
    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping("/{id}/roles")
    public Result<Void> assignRoles(@PathVariable Long id, @RequestBody @Valid AssignIdsDTO dto) {
        userService.assignRoles(id, dto);
        return Result.success();
    }
}
