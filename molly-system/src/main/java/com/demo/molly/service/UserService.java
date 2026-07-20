package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.AssignIdsDTO;
import com.demo.molly.dto.KeywordStatusQuery;
import com.demo.molly.dto.UpdateUserDTO;
import com.demo.molly.dto.UserDTO;
import com.demo.molly.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    PageResult<UserVO> list(KeywordStatusQuery query);

    UserVO detail(Long id);

    void create(UserDTO dto);

    void update(Long id, UpdateUserDTO dto);

    void delete(Long id);

    void updateStatus(Long id, Integer status);

    void assignRoles(Long userId, AssignIdsDTO dto);
}
