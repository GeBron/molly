import { get, post, put, del } from '@/utils/request'
import type {
  AssignRoleDTO,
  PageResult,
  UpdateUserDTO,
  UserDTO,
  UserQueryDTO,
  UserVO,
} from '@/types'

export function listUsers(params: UserQueryDTO) {
  return get<PageResult<UserVO>>('/users', { params })
}

export function getUser(id: number) {
  return get<UserVO>(`/users/${id}`)
}

export function createUser(data: UserDTO) {
  return post<null>('/users', data)
}

export function updateUser(id: number, data: UpdateUserDTO) {
  return put<null>(`/users/${id}`, data)
}

export function deleteUser(id: number) {
  return del<null>(`/users/${id}`)
}

export function updateUserStatus(id: number, status: number) {
  return put<null>(`/users/${id}/status`, null, { params: { status } })
}

export function assignUserRoles(id: number, data: AssignRoleDTO) {
  return post<null>(`/users/${id}/roles`, data)
}
