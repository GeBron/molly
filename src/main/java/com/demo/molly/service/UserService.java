package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.AssignRoleDTO;
import com.demo.molly.dto.UpdateUserDTO;
import com.demo.molly.dto.UserDTO;
import com.demo.molly.dto.UserQueryDTO;
import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.demo.molly.entity.Role;
import com.demo.molly.entity.User;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.UserMapper;
import com.demo.molly.util.AuditUtil;
import com.demo.molly.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户服务
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenCacheService tokenCacheService;

    @Autowired
    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder, TokenCacheService tokenCacheService) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenCacheService = tokenCacheService;
    }

    public PageResult<UserVO> list(UserQueryDTO query) {
        PageInfo<User> pageInfo = PageHelper.startPage(query.getPageNum(), query.getPageSize())
                .doSelectPageInfo(new ISelect() {
                    @Override
                    public void doSelect() {
                        userMapper.selectList(query.username(), query.status());
                    }
                });

        List<UserVO> list = new ArrayList<>();
        for (User user : pageInfo.getList()) {
            list.add(toVO(user));
        }
        return new PageResult<>(list, pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPageSize());
    }

    public UserVO detail(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return toVO(user);
    }

    @Transactional
    public void create(UserDTO dto) {
        User exist = userMapper.findByUsername(dto.username());
        if (exist != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRealName(dto.realName());
        user.setStatus(dto.status() == null ? 1 : dto.status());
        AuditUtil.fillCreate(user);
        userMapper.insert(user);
    }

    @Transactional
    public void update(Long id, UpdateUserDTO dto) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setRealName(dto.realName());
        user.setStatus(dto.status());
        AuditUtil.fillUpdate(user);
        userMapper.update(user);
        tokenCacheService.clearUserCache(id);
    }

    @Transactional
    public void delete(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if ("admin".equals(user.getUsername())) {
            throw new BusinessException("不能删除超级管理员");
        }
        userMapper.updateDeleted(id, 1, AuditUtil.currentUserId());
        tokenCacheService.clearUserCache(id);
    }

    @Transactional
    public void updateStatus(Long id, Integer status) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if ("admin".equals(user.getUsername()) && status != 1) {
            throw new BusinessException("不能禁用超级管理员");
        }
        userMapper.updateStatus(id, status, AuditUtil.currentUserId());
        tokenCacheService.clearUserCache(id);
    }

    @Transactional
    public void assignRoles(Long userId, AssignRoleDTO dto) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.deleteUserRolesByUserId(userId);
        if (dto.roleIds() != null && !dto.roleIds().isEmpty()) {
            userMapper.insertUserRoles(userId, dto.roleIds(), AuditUtil.currentUserId(), AuditUtil.currentUserId());
        }
        tokenCacheService.clearUserCache(userId);
    }

    private UserVO toVO(User user) {
        List<Role> roles = user.getRoles();
        List<Long> roleIds = new ArrayList<>();
        List<String> roleNames = new ArrayList<>();
        if (roles != null) {
            for (Role role : roles) {
                roleIds.add(role.getId());
                roleNames.add(role.getRoleName());
            }
        }
        return new UserVO(user.getId(), user.getUsername(), user.getRealName(), user.getStatus(), user.getCreatedAt(), roleIds, roleNames);
    }
}
