package com.demo.molly.dto;

import javax.validation.constraints.Size;

/**
 * 修改用户请求参数
 */
public class UpdateUserDTO {

    @Size(max = 64, message = "真实姓名长度不能超过64")
    private String realName;

    private Integer status;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String realName, Integer status) {
        this.realName = realName;
        this.status = status;
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
