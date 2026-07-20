package com.demo.molly.vo;

import java.time.LocalDateTime;

/**
 * 字典数据 VO
 */
public class DictDataVO {

    private Long id;
    private String dictType;
    private String dictLabel;
    private String dictValue;
    private Integer sort;
    private Integer isDefault;
    private Integer status;
    private String remark;
    private LocalDateTime createdAt;

    public DictDataVO() {
    }

    public DictDataVO(Long id, String dictType, String dictLabel, String dictValue, Integer sort,
                      Integer isDefault, Integer status, String remark, LocalDateTime createdAt) {
        this.id = id;
        this.dictType = dictType;
        this.dictLabel = dictLabel;
        this.dictValue = dictValue;
        this.sort = sort;
        this.isDefault = isDefault;
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

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
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
