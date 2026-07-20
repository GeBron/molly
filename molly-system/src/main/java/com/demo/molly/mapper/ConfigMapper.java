package com.demo.molly.mapper;

import com.demo.molly.entity.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConfigMapper {

    Config findById(@Param("id") Long id);

    Config findByKey(@Param("configKey") String configKey);

    List<Config> selectList(@Param("keyword") String keyword, @Param("status") Integer status);

    int insert(Config config);

    int update(Config config);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updatedBy") Long updatedBy);

    int updateDeleted(@Param("id") Long id, @Param("deleted") Integer deleted, @Param("updatedBy") Long updatedBy);
}
