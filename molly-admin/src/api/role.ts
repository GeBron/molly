import { get, post, put, del } from '@/utils/request'
import type {
  AssignPermissionDTO,
  PageResult,
  RoleDTO,
  RoleQueryDTO,
  RoleVO,
} from '@/types'

export function listRoles(params: RoleQueryDTO) {
  return get<PageResult<RoleVO>>('/roles', { params })
}

export function getRole(id: number) {
  return get<RoleVO>(`/roles/${id}`)
}

export function createRole(data: RoleDTO) {
  return post<null>('/roles', data)
}

export function updateRole(id: number, data: RoleDTO) {
  return put<null>(`/roles/${id}`, data)
}

export function deleteRole(id: number) {
  return del<null>(`/roles/${id}`)
}

export function updateRoleStatus(id: number, status: number) {
  return put<null>(`/roles/${id}/status`, null, { params: { status } })
}

export function assignRolePermissions(id: number, data: AssignPermissionDTO) {
  return post<null>(`/roles/${id}/permissions`, data)
}
