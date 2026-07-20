package com.demo.molly.vo;

import java.time.LocalDateTime;

/**
 * 系统参数 VO
 */
public class ConfigVO {

    private Long id;
    private String configKey;
    private String configValue;
    private String configName;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;

    public ConfigVO() {
    }

    public ConfigVO(Long id, String configKey, String configValue, String configName, Integer status, String remark, LocalDateTime createdAt) {
        this.id = id;
        this.configKey = configKey;
        this.configValue = configValue;
        this.configName = configName;
        this.status = status;
        this.remark = remark;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
