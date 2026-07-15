# Molly 后台管理系统 - 技术架构文档

## 1. 架构设计

```mermaid
graph LR
    A["浏览器"] -->|HTTP / HTTPS| B["molly-admin 前端"]
    B -->|JWT Bearer Token| C["molly 后端 Spring Boot"]
    C --> D["Spring Security"]
    D --> E["Controller"]
    E --> F["Service"]
    F --> G["MyBatis Mapper"]
    G --> H["TiDB Cloud MySQL"]
    E --> I["操作日志 AOP"]
    E --> J["登录日志"]
```

## 2. 技术说明

- **前端**：Vue 3 + TypeScript + Vite + Element Plus + Vue Router 4 + Pinia + Axios
- **后端**：Spring Boot 4.0.7 + Spring Security + MyBatis + MySQL/TiDB
- **认证**：JWT 单 Token，有效期 24 小时
- **权限模型**：RBAC，用户 -> 角色 -> 权限
- **数据库**：TiDB Cloud MySQL 兼容实例
- **部署**：前端静态资源由 Nginx 托管，反向代理 `/api` 到后端服务

## 3. 路由定义

| 路由 | 用途 |
|---|---|
| `/login` | 登录页 |
| `/` | 主布局，重定向到首页 |
| `/dashboard` | 首页 Dashboard |
| `/system/user` | 用户管理 |
| `/system/role` | 角色管理 |
| `/system/permission` | 权限管理 |
| `/system/login-log` | 登录日志 |
| `/system/operation-log` | 操作日志 |
| `/403` | 无权限提示页 |
| `/*` | 404 页面 |

## 4. API 定义

### 4.1 认证相关

```ts
interface LoginRequest {
  username: string
  password: string
}

interface LoginResponse {
  token: string
  tokenType: string
  expiresIn: number
}

interface UserInfo {
  id: number
  username: string
  realName: string
  roles: string[]
  permissions: string[]
  menus: Menu[]
}

interface Menu {
  id: number
  name: string
  path: string
  type: number // 1 目录 2 菜单
  children?: Menu[]
}
```

### 4.2 统一响应

```ts
interface Result<T> {
  code: number
  message: string
  data: T
}

interface PageResult<T> {
  list: T[]
  total: number
  pageNum: number
  pageSize: number
}
```

## 5. 后端架构

```mermaid
graph TD
    A["AuthController"] --> B["AuthService"]
    C["UserController"] --> D["UserService"]
    E["RoleController"] --> F["RoleService"]
    G["PermissionController"] --> H["PermissionService"]
    I["LogController"] --> J["LogService"]
    B --> K["UserDetailsServiceImpl"]
    D --> L["UserMapper"]
    F --> M["RoleMapper"]
    H --> N["PermissionMapper"]
    J --> O["LoginLogMapper / OperationLogMapper"]
    K --> L
    K --> M
    K --> N
```

## 6. 数据模型

### 6.1 ER 图

```mermaid
erDiagram
    sys_user ||--o{ sys_user_role : "拥有"
    sys_role ||--o{ sys_user_role : "被分配"
    sys_role ||--o{ sys_role_permission : "拥有"
    sys_permission ||--o{ sys_role_permission : "被分配"
    sys_user ||--o{ sys_login_log : "产生"
    sys_user ||--o{ sys_operation_log : "产生"

    sys_user {
        bigint id PK
        varchar username
        varchar password
        varchar real_name
        tinyint status
        tinyint deleted
        int login_fail_count
        datetime lock_time
        datetime created_at
        datetime updated_at
    }

    sys_role {
        bigint id PK
        varchar role_code
        varchar role_name
        tinyint status
        tinyint deleted
        datetime created_at
        datetime updated_at
    }

    sys_permission {
        bigint id PK
        varchar perm_code
        varchar perm_name
        tinyint type
        bigint parent_id
        varchar path
        int sort
        tinyint status
        tinyint deleted
        datetime created_at
        datetime updated_at
    }

    sys_user_role {
        bigint user_id FK
        bigint role_id FK
    }

    sys_role_permission {
        bigint role_id FK
        bigint permission_id FK
    }

    sys_login_log {
        bigint id PK
        bigint user_id
        varchar username
        varchar ip
        varchar operation
        varchar status
        varchar message
        datetime created_at
    }

    sys_operation_log {
        bigint id PK
        bigint user_id
        varchar username
        varchar module
        varchar operation
        varchar request_url
        varchar request_method
        varchar method
        text params
        text result
        long duration
        varchar ip
        datetime created_at
    }
```

### 6.2 数据定义

建表语句与初始化数据由后端 `src/main/resources/schema.sql` 与 `data.sql` 提供，`admin` 账号由 `SystemInitRunner` 在应用启动时生成。

