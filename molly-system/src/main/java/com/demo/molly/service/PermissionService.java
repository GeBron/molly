package com.demo.molly.service;

import com.demo.molly.dto.PermissionDTO;
import com.demo.molly.vo.PermissionVO;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {

    List<PermissionVO> tree();

    PermissionVO detail(Long id);

    void create(PermissionDTO dto);

    void update(Long id, PermissionDTO dto);

    void delete(Long id);

    void updateStatus(Long id, Integer status);
}
