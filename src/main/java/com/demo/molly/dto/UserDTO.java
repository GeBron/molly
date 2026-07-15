package com.demo.molly.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 用户请求参数
 */
public class UserDTO {

    @NotBlank(message = "用户名不能为空")
    @Size(max = 64, message = "用户名长度不能超过64")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, message = "密码长度不能小于6")
    private String password;

    @Size(max = 64, message = "真实姓名长度不能超过64")
    private String realName;

    private Integer status;

    public UserDTO() {
    }

    public UserDTO(String username, String password, String realName, Integer status) {
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.status = status;
    }

    public String username() {
        return username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String password() {
        return password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String realName() {
        return realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer status() {
        return status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
