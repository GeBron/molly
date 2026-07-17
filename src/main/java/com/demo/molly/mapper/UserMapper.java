package com.demo.molly.mapper;

import com.demo.molly.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户 Mapper
 */
@Mapper
public interface UserMapper {

    User findByUsername(String username);

    User findById(Long id);

    int insert(User user);

    int update(User user);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedBy") Long updatedBy);

    int updateDeleted(@Param("id") Long id, @Param("deleted") Integer deleted, @Param("updatedBy") Long updatedBy);

    int updateLoginFail(@Param("id") Long id,
                        @Param("loginFailCount") Integer loginFailCount,
                        @Param("lockTime") LocalDateTime lockTime,
                        @Param("updatedBy") Long updatedBy);

    int resetLoginFail(@Param("id") Long id, @Param("updatedBy") Long updatedBy);

    List<User> selectList(@Param("username") String username,
                          @Param("status") Integer status);

    List<Long> selectRoleIdsByUserId(Long userId);

    List<Long> selectUserIdsByRoleId(Long roleId);

    int insertUserRoles(@Param("userId") Long userId,
                        @Param("roleIds") List<Long> roleIds,
                        @Param("createdBy") Long createdBy,
                        @Param("updatedBy") Long updatedBy);

    int deleteUserRolesByUserId(Long userId);
}
