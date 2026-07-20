package com.demo.molly.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 系统参数 DTO
 */
public class ConfigDTO {

    @NotBlank(message = "参数键不能为空")
    private String configKey;

    @NotBlank(message = "参数值不能为空")
    private String configValue;

    @NotBlank(message = "参数名称不能为空")
    private String configName;

    private Integer status;

    private String remark;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
