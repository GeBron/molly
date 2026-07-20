package com.demo.molly.mapper;

import com.demo.molly.entity.DictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DictDataMapper {

    DictData findById(@Param("id") Long id);

    List<DictData> selectByType(@Param("dictType") String dictType);

    List<DictData> selectList(@Param("dictType") String dictType, @Param("status") Integer status);

    int insert(DictData dictData);

    int update(DictData dictData);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedBy") Long updatedBy);

    int updateDeleted(@Param("id") Long id, @Param("deleted") Integer deleted, @Param("updatedBy") Long updatedBy);
}
