import { get } from '@/utils/request'
import type { LogQueryDTO, LoginLogVO, OperationLogVO, PageResult } from '@/types'

export function loginLogs(params: LogQueryDTO) {
  return get<PageResult<LoginLogVO>>('/logs/login', { params })
}

export function operationLogs(params: LogQueryDTO) {
  return get<PageResult<OperationLogVO>>('/logs/operation', { params })
}
