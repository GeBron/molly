package com.demo.molly.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 通用 ID 列表分配请求参数
 */
public class AssignIdsDTO {

    @NotNull(message = "ID 列表不能为空")
    private List<Long> ids;

    public AssignIdsDTO() {
    }

    public AssignIdsDTO(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
