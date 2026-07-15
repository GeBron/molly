package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.AssignRoleDTO;
import com.demo.molly.dto.UpdateUserDTO;
import com.demo.molly.dto.UserDTO;
import com.demo.molly.dto.UserQueryDTO;
import com.demo.molly.entity.Role;
import com.demo.molly.entity.User;
import com.demo.molly.exception.BusinessException;
import com.demo.molly.mapper.RoleMapper;
import com.demo.molly.mapper.UserMapper;
import com.demo.molly.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务
 */
@Service
public class UserService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, RoleMapper roleMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public PageResult<UserVO> list(UserQueryDTO query) {
        int pageNum = query.pageNum() == null ? 1 : query.pageNum();
        int pageSize = query.pageSize() == null ? 10 : query.pageSize();
        int offset = (pageNum - 1) * pageSize;

        List<User> users = userMapper.selectList(query.username(), query.status(), offset, pageSize);
        long total = userMapper.count(query.username(), query.status());

        List<UserVO> list = users.stream().map(this::toVO).toList();
        return new PageResult<>(list, total, pageNum, pageSize);
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
        userMapper.update(user);
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
        userMapper.updateDeleted(id, 1);
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
        userMapper.updateStatus(id, status);
    }

    @Transactional
    public void assignRoles(Long userId, AssignRoleDTO dto) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.deleteUserRolesByUserId(userId);
        if (dto.roleIds() != null && !dto.roleIds().isEmpty()) {
            userMapper.insertUserRoles(userId, dto.roleIds());
        }
    }

    private UserVO toVO(User user) {
        List<Role> roles = roleMapper.selectByUserId(user.getId());
        List<Long> roleIds = roles.stream().map(Role::getId).toList();
        List<String> roleNames = roles.stream().map(Role::getRoleName).toList();
        return new UserVO(user.getId(), user.getUsername(), user.getRealName(), user.getStatus(), user.getCreatedAt(), roleIds, roleNames);
    }
}
