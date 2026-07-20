package com.demo.molly.service.impl;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.AssignIdsDTO;
import com.demo.molly.dto.KeywordStatusQuery;
import com.demo.molly.dto.RoleDTO;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.demo.molly.entity.Role;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.RoleMapper;
import com.demo.molly.mapper.UserMapper;
import com.demo.molly.projection.RoleWithPermissions;
import com.demo.molly.service.RoleService;
import com.demo.molly.service.TokenCacheService;
import com.demo.molly.util.AuditUtil;
import com.demo.molly.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final UserMapper userMapper;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public RoleServiceImpl(RoleMapper roleMapper, UserMapper userMapper, TokenCacheService tokenCacheService) {
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.tokenCacheService = tokenCacheService;
    }

    public PageResult<RoleVO> list(KeywordStatusQuery query) {
        PageInfo<RoleWithPermissions> pageInfo = PageHelper.startPage(query.getPageNum(), query.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        roleMapper.selectList(query.getKeyword(), query.getStatus());
                    }
                });

        List<RoleVO> list = new ArrayList<>();
        for (RoleWithPermissions role : pageInfo.getList()) {
            list.add(toVO(role));
        }
        return new PageResult<>(list, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
    }

    public RoleVO detail(Long id) {
        RoleWithPermissions role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return toVO(role);
    }

    @Transactional
    public void create(RoleDTO dto) {
        Role exist = roleMapper.findByCode(dto.getRoleCode());
        if (exist != null) {
            throw new BusinessException("角色编码已存在");
        }
        Role role = new Role();
        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        AuditUtil.fillCreate(role);
        roleMapper.insert(role);
    }

    @Transactional
    public void update(Long id, RoleDTO dto) {
        Role role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        Role exist = roleMapper.findByCode(dto.getRoleCode());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException("角色编码已存在");
        }
        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setStatus(dto.getStatus());
        AuditUtil.fillUpdate(role);
        roleMapper.update(role);
        clearUserCachesByRoleId(id);
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
        clearUserCachesByRoleId(id);
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
        clearUserCachesByRoleId(id);
    }

    @Transactional
    public void assignPermissions(Long roleId, AssignIdsDTO dto) {
        Role role = roleMapper.findById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        roleMapper.deleteRolePermissionsByRoleId(roleId);
        if (dto.getIds() != null && !dto.getIds().isEmpty()) {
            roleMapper.insertRolePermissions(roleId, dto.getIds(), AuditUtil.currentUserId(), AuditUtil.currentUserId());
        }
        List<Long> userIds = userMapper.selectUserIdsByRoleId(roleId);
        for (Long userId : userIds) {
            tokenCacheService.clearUserCache(userId);
        }
    }

    private void clearUserCachesByRoleId(Long roleId) {
        List<Long> userIds = userMapper.selectUserIdsByRoleId(roleId);
        for (Long userId : userIds) {
            tokenCacheService.clearUserCache(userId);
        }
    }

    private RoleVO toVO(RoleWithPermissions role) {
        List<Long> permissionIds = role.getPermissionIds();
        return new RoleVO(role.getId(), role.getRoleCode(), role.getRoleName(), role.getStatus(), role.getCreatedAt(), permissionIds);
    }
}
