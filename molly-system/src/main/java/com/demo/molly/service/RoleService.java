package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.AssignIdsDTO;
import com.demo.molly.dto.KeywordStatusQuery;
import com.demo.molly.dto.RoleDTO;
import com.demo.molly.vo.RoleVO;

/**
 * 角色服务接口
 */
public interface RoleService {

    PageResult<RoleVO> list(KeywordStatusQuery query);

    RoleVO detail(Long id);

    void create(RoleDTO dto);

    void update(Long id, RoleDTO dto);

    void delete(Long id);

    void updateStatus(Long id, Integer status);

    void assignPermissions(Long roleId, AssignIdsDTO dto);
}
