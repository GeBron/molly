package com.demo.molly.mapper;

import com.demo.molly.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志 Mapper
 */
@Mapper
public interface OperationLogMapper {

    int insert(OperationLog log);

    List<OperationLog> selectList(@Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);
}
