package com.demo.molly.service;

import com.demo.molly.dto.PermissionDTO;
import com.demo.molly.entity.Permission;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.PermissionMapper;
import com.demo.molly.util.AuditUtil;
import com.demo.molly.vo.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限服务
 */
@Service
public class PermissionService {

    private final PermissionMapper permissionMapper;

    @Autowired
    public PermissionService(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

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
    public void create(PermissionDTO dto) {
        Permission exist = permissionMapper.findByCode(dto.permCode());
        if (exist != null) {
            throw new BusinessException("权限编码已存在");
        }
        Permission permission = new Permission();
        permission.setPermCode(dto.permCode());
        permission.setPermName(dto.permName());
        permission.setType(dto.type());
        permission.setParentId(dto.parentId() == null ? 0L : dto.parentId());
        permission.setPath(dto.path());
        permission.setSort(dto.sort() == null ? 0 : dto.sort());
        permission.setStatus(dto.status() == null ? 1 : dto.status());
        AuditUtil.fillCreate(permission);
        permissionMapper.insert(permission);
    }

    @Transactional
    public void update(Long id, PermissionDTO dto) {
        Permission permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        Permission exist = permissionMapper.findByCode(dto.permCode());
        if (exist != null && !exist.getId().equals(id)) {
            throw new BusinessException("权限编码已存在");
        }
        permission.setPermCode(dto.permCode());
        permission.setPermName(dto.permName());
        permission.setType(dto.type());
        permission.setParentId(dto.parentId() == null ? 0L : dto.parentId());
        permission.setPath(dto.path());
        permission.setSort(dto.sort() == null ? 0 : dto.sort());
        permission.setStatus(dto.status());
        AuditUtil.fillUpdate(permission);
        permissionMapper.update(permission);
    }

    @Transactional
    public void delete(Long id) {
        Permission permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        permissionMapper.updateDeleted(id, 1, AuditUtil.currentUserId());
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        Permission permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException("权限不存在");
        }
        permissionMapper.updateStatus(id, status, AuditUtil.currentUserId());
    }

    private List<PermissionVO> buildTree(List<Permission> permissions) {
        List<Permission> sorted = permissions.stream()
                .sorted(Comparator.comparingInt(p -> p.getSort() == null ? 0 : p.getSort()))
                .toList();
        Map<Long, PermissionVO> map = sorted.stream()
                .collect(Collectors.toMap(Permission::getId, p -> toVO(p, new ArrayList<>())));

        List<PermissionVO> roots = new ArrayList<>();
        for (Permission p : sorted) {
            PermissionVO vo = map.get(p.getId());
            if (p.getParentId() == null || p.getParentId() == 0L) {
                roots.add(vo);
            } else {
                PermissionVO parent = map.get(p.getParentId());
                if (parent != null && parent.children() != null) {
                    parent.children().add(vo);
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
