package com.demo.molly.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 修改用户请求参数
 */
public class UpdateUserDTO {

    @Size(max = 64, message = "真实姓名长度不能超过64")
    private String realName;

    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不合法")
    @Max(value = 1, message = "状态值不合法")
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
