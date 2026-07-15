import { get, post, put, del } from '@/utils/request'
import type { PermissionDTO, PermissionVO } from '@/types'

export function permissionTree() {
  return get<PermissionVO[]>('/permissions')
}

export function getPermission(id: number) {
  return get<PermissionVO>(`/permissions/${id}`)
}

export function createPermission(data: PermissionDTO) {
  return post<null>('/permissions', data)
}

export function updatePermission(id: number, data: PermissionDTO) {
  return put<null>(`/permissions/${id}`, data)
}

export function deletePermission(id: number) {
  return del<null>(`/permissions/${id}`)
}

export function updatePermissionStatus(id: number, status: number) {
  return put<null>(`/permissions/${id}/status`, null, { params: { status } })
}
