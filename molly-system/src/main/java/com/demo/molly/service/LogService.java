package com.demo.molly.service;

import com.demo.molly.common.PageResult;
import com.demo.molly.dto.LogQueryDTO;
import com.demo.molly.vo.LoginLogVO;
import com.demo.molly.vo.OperationLogVO;

/**
 * 日志服务接口
 */
public interface LogService {

    PageResult<LoginLogVO> loginLogList(LogQueryDTO query);

    PageResult<OperationLogVO> operationLogList(LogQueryDTO query);
}
