package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.AssignPermissionDTO;
import com.demo.molly.dto.RoleDTO;
import com.demo.molly.dto.RoleQueryDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.demo.molly.entity.Role;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.RoleMapper;
import com.demo.molly.mapper.UserMapper;
import com.demo.molly.util.AuditUtil;
import com.demo.molly.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务
 */
@Service
public class RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public RoleService(RoleMapper roleMapper, UserMapper userMapper, TokenCacheService tokenCacheService) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.tokenCacheService = tokenCacheService;
    }

    public PageResult<RoleVO> list(RoleQueryDTO query) {
        PageInfo<Role> pageInfo = PageHelper.startPage(query.getPageNum(), query.getPageSize())
                .doSelectPageInfo(() -> roleMapper.selectList(query.roleName(), query.status()));

        List<RoleVO> list = pageInfo.getList().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(list, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
    }

    public RoleVO detail(Long id) {
        Role role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return toVO(role);
    }

    @Transactional
    public void create(RoleDTO dto) {
        Role exist = roleMapper.findByCode(dto.roleCode());
        if (exist != null) {
            throw new BusinessException("角色编码已存在");
        }
        Role role = new Role();
        role.setRoleCode(dto.roleCode());
        role.setRoleName(dto.roleName());
        role.setStatus(dto.status() == null ? 1 : dto.status());
        AuditUtil.fillCreate(role);
        roleMapper.insert(role);
    }

    @Transactional
    public void update(Long id, RoleDTO dto) {
        Role role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        Role exist = roleMapper.findByCode(dto.roleCode());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException("角色编码已存在");
        }
        role.setRoleCode(dto.roleCode());
        role.setRoleName(dto.roleName());
        role.setStatus(dto.status());
        AuditUtil.fillUpdate(role);
        roleMapper.update(role);
    }

    @Transactional
    public void delete(Long id) {
        Role role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        if ("admin".equals(role.getRoleCode())) {
            throw new BusinessException("不能删除超级管理员角色");
        }
        roleMapper.updateDeleted(id, 1, AuditUtil.currentUserId());
        roleMapper.deleteRolePermissionsByRoleId(id);
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        Role role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        if ("admin".equals(role.getRoleCode()) && status != 1) {
            throw new BusinessException("不能禁用超级管理员角色");
        }
        roleMapper.updateStatus(id, status, AuditUtil.currentUserId());
    }

    @Transactional
    public void assignPermissions(Long roleId, AssignPermissionDTO dto) {
        Role role = roleMapper.findById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        roleMapper.deleteRolePermissionsByRoleId(roleId);
        if (dto.permissionIds() != null && !dto.permissionIds().isEmpty()) {
            roleMapper.insertRolePermissions(roleId, dto.permissionIds(), AuditUtil.currentUserId(), AuditUtil.currentUserId());
        }
        List<Long> userIds = userMapper.selectUserIdsByRoleId(roleId);
        for (Long userId : userIds) {
            tokenCacheService.clearUserCache(userId);
        }
    }

    private RoleVO toVO(Role role) {
        List<Long> permissionIds = role.getPermissionIds();
        return new RoleVO(role.getId(), role.getRoleCode(), role.getRoleName(), role.getStatus(), role.getCreatedAt(), permissionIds);
    }
}
