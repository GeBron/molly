package com.demo.molly.mapper;

import com.demo.molly.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限 Mapper
 */
@Mapper
public interface PermissionMapper {

    Permission findById(Long id);

    Permission findByCode(String permCode);

    int insert(Permission permission);

    int update(Permission permission);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedBy") Long updatedBy);

    int updateDeleted(@Param("id") Long id, @Param("deleted") Integer deleted, @Param("updatedBy") Long updatedBy);

    List<Permission> selectAll();

    List<Permission> selectByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<Permission> selectByType(@Param("type") Integer type);

    List<Permission> selectList(@Param("permName") String permName, @Param("status") Integer status);
}
