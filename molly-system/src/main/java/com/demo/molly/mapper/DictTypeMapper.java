package com.demo.molly.mapper;

import com.demo.molly.entity.DictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictTypeMapper {

    DictType findById(@Param("id") Long id);

    DictType findByType(@Param("dictType") String dictType);

    List<DictType> selectList(@Param("keyword") String keyword, @Param("status") Integer status);

    int insert(DictType dictType);

    int update(DictType dictType);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedBy") Long updatedBy);

    int updateDeleted(@Param("id") Long id, @Param("deleted") Integer deleted, @Param("updatedBy") Long updatedBy);
}
