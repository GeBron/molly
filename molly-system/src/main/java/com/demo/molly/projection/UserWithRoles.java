package com.demo.molly.projection;

import com.demo.molly.entity.Role;
import com.demo.molly.entity.User;

import java.util.List;

/**
 * 用户及其角色投影（将关联字段从 User 实体中分离）
 */
public class UserWithRoles extends User {

    private List<Role> roles;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
