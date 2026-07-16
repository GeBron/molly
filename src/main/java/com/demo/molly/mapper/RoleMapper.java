package com.demo.molly.mapper;

import com.demo.molly.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper
 */
@Mapper
public interface RoleMapper {

    Role findById(Long id);

    Role findByCode(String roleCode);

    int insert(Role role);

    int update(Role role);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedBy") Long updatedBy);

    int updateDeleted(@Param("id") Long id, @Param("deleted") Integer deleted, @Param("updatedBy") Long updatedBy);

    List<Role> selectList(@Param("roleName") String roleName,
                          @Param("status") Integer status);

    List<Role> selectByUserId(Long userId);

    List<Long> selectPermissionIdsByRoleId(Long roleId);

    int insertRolePermissions(@Param("roleId") Long roleId,
                              @Param("permissionIds") List<Long> permissionIds,
                              @Param("createdBy") Long createdBy,
                              @Param("updatedBy") Long updatedBy);

    int deleteRolePermissionsByRoleId(Long roleId);
}
