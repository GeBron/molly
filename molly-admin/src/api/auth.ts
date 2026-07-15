import { get, post } from '@/utils/request'
import type { LoginDTO, LoginVO, UserInfoVO } from '@/types'

export function login(data: LoginDTO) {
  return post<LoginVO>('/auth/login', data)
}

export function logout() {
  return post<null>('/auth/logout')
}

export function info() {
  return get<UserInfoVO>('/auth/info')
}
