# molly

Molly 后台管理系统（前后端分离）

## 项目信息

| 项目 | 说明 |
| --- | --- |
| 项目名 | molly |
| 包名 | `com.demo.molly` |
| 构建工具 | Maven |
| Spring Boot | 3.2.7 |
| Java | 17 |
| 数据库 | TiDB Cloud（MySQL 兼容） |
| 缓存 | Redis |
| 前端 | Vue 3 + TypeScript + Vite + Element Plus |

## 技术栈

### 后端

- Java 17
- Spring Boot 3.2.7
- Spring Web
- Spring Security
- Spring Validation
- Spring AOP
- Spring Data Redis
- JWT（jjwt 0.12.6）
- MyBatis Spring Boot Starter 3.0.4
- MySQL Connector/J
- Flyway

### 前端

- Vue 3
- TypeScript
- Vite
- Vue Router
- Element Plus
- Axios
- Tailwind CSS
- Lucide Vue

## 主要功能

- **RBAC 权限控制**：用户、角色、权限多对多关系，支持页面菜单、按钮、接口级权限
- **双 Token 认证**：Access Token（默认 2 小时）+ Refresh Token（默认 7 天），Refresh Token 以 httpOnly Cookie 下发
- **Token 安全管理**：支持 Token 黑名单（Redis）与无感刷新，登出时立即使 Token 失效
- **用户权限缓存**：用户角色与权限缓存至 Redis，修改角色分配后自动清理缓存
- **登录安全**：连续失败 5 次锁定账号 30 分钟，超过 30 分钟自动解锁
- **系统日志**：AOP 记录操作日志（异步保存），登录/登出记录登录日志；支持敏感字段脱敏
- **统一错误码**：使用 `ErrorCode` 枚举统一封装异常响应
- **审计字段**：所有业务表统一使用 `created_by`、`updated_by`、`created_at`、`updated_at`、`deleted`
- **逻辑删除**：统一使用 `deleted` 字段进行软删除
- **动态菜单路由**：前端根据 `/api/auth/info` 返回的菜单动态生成路由
- **初始化数据**：Flyway 版本化迁移脚本自动建表并初始化超级管理员账号（`dev` / `prod` 统一使用）

## 环境要求

- JDK 17 及以上
- Maven 3.9 及以上
- Node.js 18 及以上（前端）
- Redis 服务（本地或远程，默认 `localhost:6379`）
- TiDB Cloud 账号（项目已使用位于 `ap-southeast-1` 区域的 TiDB Cloud 集群，地址：`gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000`）

## 快速开始

### 1. 克隆代码

```bash
git clone https://github.com/GeBron/molly.git
cd molly
```

### 2. 配置环境变量

连接信息以环境变量方式注入，`src/main/resources/application.yml` 中已引用这些变量，无需手动修改配置文件。项目已按 `dev` / `prod` 拆分配置：

- `application.yml`：公共配置，默认激活 `dev` 环境
- `application-dev.yml`：开发环境配置
- `application-prod.yml`：生产环境配置

启动前在系统中设置环境变量：

```bash
export TiDB_USERNAME=<your_tidb_username>
export TiDB_PASSWORD=<your_tidb_password>
export JWT_ACCESS_SECRET=<your_access_secret>
export JWT_REFRESH_SECRET=<your_refresh_secret>
export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=
export REDIS_DB=0
```

对应的数据源配置如下（以 `application-dev.yml` 为例）：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${TiDB_USERNAME}:${TiDB_PASSWORD}@gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000/molly?sslMode=VERIFY_IDENTITY
    username: ${TiDB_USERNAME}
    password: ${TiDB_PASSWORD}
    driver-class-name: com.mysql.cj.jdbc.Driver
```

> 请在 TiDB Cloud 控制台创建集群与数据库 `molly`，并确保当前网络可访问 `gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000`（TiDB Cloud 默认要求 TLS 加密连接）。

### 3. 启动后端

```bash
mvn spring-boot:run
```

应用启动后默认监听 `8080` 端口，启动时会自动执行 Flyway 迁移脚本（`db/migration/V1__init_schema.sql`、`V2__init_data.sql`）初始化表结构和基础数据。迁移脚本仅首次执行，后续启动会跳过已执行的版本。

默认超级管理员账号：

- 用户名：`admin`
- 密码：`admin123`

### 4. 切换环境（可选）

本地开发默认使用 `dev` 环境，生产环境启动时显式指定 `prod`：

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

或直接运行 jar：

```bash
java -jar target/molly-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

### 5. 启动前端

```bash
cd molly-admin
npm install
npm run dev
```

前端默认运行在 `http://localhost:5173`，并已配置跨域代理到后端 `http://localhost:8080`。

## 主要接口

| 接口 | 说明 |
| --- | --- |
| `POST /api/auth/login` | 登录，返回 Access Token，Refresh Token 写入 Cookie |
| `POST /api/auth/refresh` | 使用 Cookie 中的 Refresh Token 换取新的双 Token |
| `POST /api/auth/logout` | 登出，并将当前 Access/Refresh Token 加入黑名单 |
| `GET /api/auth/info` | 获取当前登录用户信息、角色、权限与菜单 |
| `GET /api/users` | 用户列表 |
| `GET /api/roles` | 角色列表 |
| `GET /api/permissions` | 权限树 |
| `GET /api/logs/login` | 登录日志 |
| `GET /api/logs/operation` | 操作日志 |

## 开发步骤

1. 后端代码在 `src/main/java/com/demo/molly` 下，建议按 `controller / service / mapper / entity / config / security / common / util / aspect / exception / vo / dto` 分包。
2. MyBatis 映射文件放置在 `src/main/resources/mapper` 目录下。
3. 前端代码在 `molly-admin/src` 下，页面位于 `molly-admin/src/pages`，API 请求位于 `molly-admin/src/api`，组合式函数位于 `molly-admin/src/composables`，自定义指令位于 `molly-admin/src/directives`。
4. 单元测试写在 `src/test/java/com/demo/molly` 下，运行 `mvn test` 触发测试。

## 常用命令

### 后端

| 命令 | 说明 |
| --- | --- |
| `mvn clean` | 清理构建产物 |
| `mvn compile` | 编译主代码 |
| `mvn test` | 运行测试 |
| `mvn package` | 打包（默认生成可执行 jar） |
| `mvn spring-boot:run` | 本地启动应用 |

### 前端

| 命令 | 说明 |
| --- | --- |
| `npm install` | 安装依赖 |
| `npm run dev` | 启动开发服务器 |
| `npm run build` | 生产构建 |
| `npm run check` | TypeScript 类型检查 |
| `npm run lint` | 运行 ESLint |
| `npm run lint:fix` | 自动修复 ESLint 问题 |

## 部署

### 后端部署

```bash
mvn clean package -DskipTests
java -jar target/molly-0.0.1-SNAPSHOT.jar
```

生产环境建议通过环境变量或外部配置中心注入数据库账号、Redis 密码、JWT 密钥等敏感信息，避免硬编码在配置文件中。生产环境已配置 `spring.flyway.clean-disabled=true`，防止误执行 Flyway `clean` 清除数据。

### 前端部署

```bash
cd molly-admin
npm run build
```

构建产物位于 `molly-admin/dist`，可部署到任意静态资源服务器。

## 目录结构

```
molly
├── molly-admin/                # 前端项目
│   ├── src/
│   │   ├── api/                # API 请求
│   │   ├── components/         # 公共组件
│   │   ├── composables/        # 组合式函数
│   │   ├── directives/         # 自定义指令
│   │   ├── pages/              # 页面
│   │   ├── router/             # 路由
│   │   ├── types/              # TypeScript 类型
│   │   ├── utils/              # 工具函数
│   │   ├── App.vue
│   │   └── main.ts
│   ├── package.json
│   └── vite.config.ts
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/demo/molly/
    │   │   ├── aspect/         # 操作日志 AOP
    │   │   ├── common/         # 通用结果封装与错误码
    │   │   ├── config/         # 配置类
    │   │   ├── controller/     # 控制器
    │   │   ├── dto/            # 请求参数
    │   │   ├── entity/         # 实体类
    │   │   ├── exception/      # 异常处理
    │   │   ├── mapper/         # MyBatis Mapper
    │   │   ├── security/       # Spring Security + JWT
    │   │   ├── service/        # 业务逻辑
    │   │   ├── util/           # 工具类
    │   │   ├── vo/             # 视图对象
    │   │   └── MollyApplication.java
    │   └── resources/
    │       ├── mapper/         # MyBatis XML
    │       ├── application.yml              # 公共配置
    │       ├── application-dev.yml          # 开发环境配置
    │       ├── application-prod.yml         # 生产环境配置
    │       └── db/migration/                # Flyway 迁移脚本
    │           ├── V1__init_schema.sql      # 建表语句
    │           └── V2__init_data.sql        # 初始化数据
    └── test/
        └── java/com/demo/molly/
```

## 许可证

本项目仅作示例用途，如需开源发布请补充合适的 License。
