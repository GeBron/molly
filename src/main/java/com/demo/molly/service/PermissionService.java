package com.demo.molly.service;

import com.demo.molly.dto.PermissionDTO;
import com.demo.molly.entity.Permission;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.PermissionMapper;
import com.demo.molly.util.AuditUtil;
import com.demo.molly.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限服务
 */
@Service
public class PermissionService {

    private final PermissionMapper permissionMapper;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public PermissionService(PermissionMapper permissionMapper, TokenCacheService tokenCacheService) {
        this.permissionMapper = permissionMapper;
        this.tokenCacheService = tokenCacheService;
    }

    @Cacheable(value = "permission:tree")
    public List<PermissionVO> tree() {
        List<Permission> permissions = permissionMapper.selectAll();
        return buildTree(permissions);
    }

    public PermissionVO detail(Long id) {
        Permission permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        return toVO(permission, null);
    }

    @Transactional
    @CacheEvict(value = "permission:tree", allEntries = true)
    public void create(PermissionDTO dto) {
        Permission exist = permissionMapper.findByCode(dto.getPermCode());
        if (exist != null) {
            throw new BusinessException("权限编码已存在");
        }
        Permission permission = new Permission();
        permission.setPermCode(dto.getPermCode());
        permission.setPermName(dto.getPermName());
        permission.setType(dto.getType());
        permission.setParentId(dto.getParentId() == null ? 0L : dto.getParentId());
        permission.setPath(dto.getPath());
        permission.setSort(dto.getSort() == null ? 0 : dto.getSort());
        permission.setStatus(dto.getStatus() == null ? 1 : dto.getStatus());
        AuditUtil.fillCreate(permission);
        permissionMapper.insert(permission);
        tokenCacheService.clearAllUserCaches();
    }

    @Transactional
    @CacheEvict(value = "permission:tree", allEntries = true)
    public void update(Long id, PermissionDTO dto) {
        Permission permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        Permission exist = permissionMapper.findByCode(dto.getPermCode());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException("权限编码已存在");
        }
        permission.setPermCode(dto.getPermCode());
        permission.setPermName(dto.getPermName());
        permission.setType(dto.getType());
        permission.setParentId(dto.getParentId() == null ? 0L : dto.getParentId());
        permission.setPath(dto.getPath());
        permission.setSort(dto.getSort() == null ? 0 : dto.getSort());
        permission.setStatus(dto.getStatus());
        AuditUtil.fillUpdate(permission);
        permissionMapper.update(permission);
        tokenCacheService.clearAllUserCaches();
    }

    @Transactional
    @CacheEvict(value = "permission:tree", allEntries = true)
    public void delete(Long id) {
        Permission permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        permissionMapper.updateDeleted(id, 1, AuditUtil.currentUserId());
        tokenCacheService.clearAllUserCaches();
    }

    @Transactional
    @CacheEvict(value = "permission:tree", allEntries = true)
    public void updateStatus(Long id, Integer status) {
        Permission permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        permissionMapper.updateStatus(id, status, AuditUtil.currentUserId());
        tokenCacheService.clearAllUserCaches();
    }

    private List<PermissionVO> buildTree(List<Permission> permissions) {
        List<Permission> sorted = new ArrayList<>(permissions);
        Collections.sort(sorted, new Comparator<Permission>() {
            @Override
            public int compare(Permission o1, Permission o2) {
                Integer s1 = o1.getSort() == null ? 0 : o1.getSort();
                Integer s2 = o2.getSort() == null ? 0 : o2.getSort();
                return s1.compareTo(s2);
            }
        });
        Map<Long, PermissionVO> map = new HashMap<>();
        for (Permission p : sorted) {
            map.put(p.getId(), toVO(p, new ArrayList<>()));
        }

        List<PermissionVO> roots = new ArrayList<>();
        for (Permission p : sorted) {
            PermissionVO vo = map.get(p.getId());
            if (p.getParentId() == null || p.getParentId() == 0L) {
                roots.add(vo);
            } else {
                PermissionVO parent = map.get(p.getParentId());
                if (parent != null && parent.getChildren() != null) {
                    parent.getChildren().add(vo);
                }
            }
        }
        return roots;
    }

    private PermissionVO toVO(Permission p, List<PermissionVO> children) {
        return new PermissionVO(
                p.getId(),
                p.getPermCode(),
                p.getPermName(),
                p.getType(),
                p.getParentId(),
                p.getPath(),
                p.getSort(),
                p.getStatus(),
                p.getCreatedAt(),
                children
        );
    }
}
