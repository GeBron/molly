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
| 前端 | Vue 3 + TypeScript + Vite + Element Plus |

## 技术栈

### 后端

- Java 17
- Spring Boot 3.2.7
- Spring Web
- Spring Security
- Spring Validation
- Spring AOP
- JWT（jjwt 0.12.6）
- MyBatis Spring Boot Starter 3.0.4
- MySQL Connector/J

### 前端

- Vue 3
- TypeScript
- Vite
- Vue Router
- Element Plus
- Axios
- Tailwind CSS

## 主要功能

- **RBAC 权限控制**：用户、角色、权限多对多关系，支持页面菜单、按钮、接口级权限
- **JWT 认证**：长时效 Token，默认 24 小时有效期
- **登录安全**：连续失败 5 次锁定账号 30 分钟
- **系统日志**：AOP 记录操作日志，登录/登出记录登录日志
- **逻辑删除**：统一使用 `deleted` 字段进行软删除
- **初始化数据**：`schema.sql` + `data.sql` 自动建表并初始化超级管理员账号

## 环境要求

- JDK 17 及以上
- Maven 3.9 及以上
- Node.js 18 及以上（前端）
- TiDB Cloud 账号（项目已使用位于 `ap-southeast-1` 区域的 TiDB Cloud 集群，地址：`gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000`）

## 快速开始

### 1. 克隆代码

```bash
git clone https://github.com/GeBron/molly.git
cd molly
```

### 2. 配置 TiDB Cloud 凭据

连接信息以环境变量方式注入，`src/main/resources/application.properties` 中已引用这些变量，无需手动修改配置文件。

启动前在系统中设置环境变量：

```bash
export TiDB_USERNAME=<your_tidb_username>
export TiDB_PASSWORD=<your_tidb_password>
```

对应的数据源配置如下：

```properties
spring.datasource.url=jdbc:mysql://${TiDB_USERNAME}:${TiDB_PASSWORD}@gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000/molly?sslMode=VERIFY_IDENTITY
spring.datasource.username=${TiDB_USERNAME}
spring.datasource.password=${TiDB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

> 请在 TiDB Cloud 控制台创建集群与数据库 `molly`，并确保当前网络可访问 `gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000`（TiDB Cloud 默认要求 TLS 加密连接）。

### 3. 启动后端

```bash
mvn spring-boot:run
```

应用启动后默认监听 `8080` 端口，启动时会自动执行 `schema.sql` 与 `data.sql` 初始化表结构和基础数据。

默认超级管理员账号：

- 用户名：`admin`
- 密码：`admin123`

### 4. 启动前端

```bash
cd molly-admin
pnpm install
pnpm run dev
```

前端默认运行在 `http://localhost:5173`，并已配置跨域代理到后端 `http://localhost:8080`。

## 主要接口

| 接口 | 说明 |
| --- | --- |
| `POST /api/auth/login` | 登录 |
| `POST /api/auth/logout` | 登出 |
| `GET /api/auth/info` | 获取当前登录用户信息、菜单、权限 |
| `GET /api/users` | 用户列表 |
| `GET /api/roles` | 角色列表 |
| `GET /api/permissions` | 权限树 |
| `GET /api/logs/login` | 登录日志 |
| `GET /api/logs/operation` | 操作日志 |

## 开发步骤

1. 后端代码在 `src/main/java/com/demo/molly` 下，建议按 `controller / service / mapper / entity / config / security` 分包。
2. MyBatis 映射文件放置在 `src/main/resources/mapper` 目录下。
3. 前端代码在 `molly-admin/src` 下，页面位于 `molly-admin/src/pages`，API 请求位于 `molly-admin/src/api`。
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
| `pnpm install` | 安装依赖 |
| `pnpm run dev` | 启动开发服务器 |
| `pnpm run build` | 生产构建 |
| `pnpm run check` | TypeScript 类型检查 |

## 部署

### 后端部署

```bash
mvn clean package -DskipTests
java -jar target/molly-0.0.1-SNAPSHOT.jar
```

生产环境建议通过环境变量或外部配置中心注入数据库账号、JWT 密钥等敏感信息，避免硬编码在配置文件中。

### 前端部署

```bash
cd molly-admin
pnpm run build
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
    │   │   ├── common/         # 通用结果封装
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
    │       ├── application.properties
    │       ├── schema.sql      # 建表语句
    │       └── data.sql        # 初始化数据
    └── test/
        └── java/com/demo/molly/
```

## 许可证

本项目仅作示例用途，如需开源发布请补充合适的 License。
