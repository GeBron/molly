export interface Result<T> {
  code: number
  message: string
  data: T
}

export interface PageResult<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
}

export interface PageQuery {
  pageNum?: number
  pageSize?: number
}

export interface LoginDTO {
  username: string
  password: string
}

export interface LoginVO {
  token: string
  tokenType: string
  expiresIn: number
}

export interface UserVO {
  id: number
  username: string
  realName: string
  status: number
  createdAt: string
  roleIds: number[] | null
  roleNames: string[] | null
}

export interface MenuVO {
  id: number
  name: string
  path: string
  type: number
  children: MenuVO[]
}

export interface UserInfoVO {
  user: UserVO
  roles: string[]
  permissions: string[]
  menus: MenuVO[]
}

export interface UserDTO {
  username: string
  password: string
  realName?: string
  status?: number
}

export interface UpdateUserDTO {
  realName?: string
  status?: number
}

export interface UserQueryDTO extends PageQuery {
  username?: string
  status?: number
}

export interface RoleVO {
  id: number
  roleCode: string
  roleName: string
  status: number
  createdAt: string
  permissionIds: number[]
}

export interface RoleDTO {
  roleCode: string
  roleName: string
  status?: number
}

export interface RoleQueryDTO extends PageQuery {
  roleName?: string
  status?: number
}

export interface PermissionVO {
  id: number
  permCode: string
  permName: string
  type: number
  parentId: number
  path?: string
  sort: number
  status: number
  createdAt: string
  children?: PermissionVO[]
}

export interface PermissionDTO {
  permCode: string
  permName: string
  type: number
  parentId?: number
  path?: string
  sort?: number
  status?: number
}

export interface AssignRoleDTO {
  roleIds: number[]
}

export interface AssignPermissionDTO {
  permissionIds: number[]
}

export interface LoginLogVO {
  id: number
  userId?: number
  username?: string
  ip?: string
  operation: string
  status: string
  message?: string
  createdAt: string
}

export interface OperationLogVO {
  id: number
  userId?: number
  username?: string
  module?: string
  operation?: string
  requestUrl?: string
  requestMethod?: string
  method?: string
  params?: string
  result?: string
  duration?: number
  ip?: string
  createdAt: string
}

export interface LogQueryDTO extends PageQuery {
  startTime?: string
  endTime?: string
}
