package com.demo.molly.mapper;

import com.demo.molly.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 登录日志 Mapper
 */
@Mapper
public interface LoginLogMapper {

    int insert(LoginLog log);

    List<LoginLog> selectList(@Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime);
}
