# molly

Molly 后台管理系统（非前后端分离版本），基于 Spring Boot + Thymeleaf 服务端渲染的 jQuery + Bootstrap 多页面应用，使用 Spring Security Session/Cookie 登录鉴权。

## 项目信息

| 项目 | 说明 |
| --- | --- |
| 项目名 | molly |
| 包名 | `com.demo.molly` |
| 构建工具 | Maven |
| Spring Boot | 2.7.18 |
| Java | 1.8 |
| 数据库 | H2（MySQL 兼容模式） |
| 缓存 | Caffeine（本地缓存） |
| 模板引擎 | Thymeleaf |
| 前端 | jQuery + Bootstrap 5 + DataTables + jsTree + flatpickr（CDN） |

## 技术栈

### 后端

- Java 1.8
- Spring Boot 2.7.18
- Spring Web
- Spring Security（Session/Cookie 登录）
- Spring Validation
- Spring AOP
- Spring Cache + Caffeine
- Thymeleaf
- MyBatis Spring Boot Starter 2.3.2
- PageHelper 1.4.7（MyBatis 分页插件）
- H2 Database（MySQL 兼容模式）

### 前端

- jQuery 3.7.1
- Bootstrap 5.3.2
- Bootstrap Icons 1.11.1
- DataTables 1.13.8（表格分页）
- jsTree 3.3.16（权限树）
- flatpickr 4.6.13（日期时间范围）

页面模板位于 `src/main/resources/templates/`，由 Thymeleaf 渲染；静态资源（CSS/JS/图标）位于 `src/main/resources/static/`。所有前端依赖均通过 CDN 引入。

## 环境要求

- JDK 1.8 及以上
- Maven 3.6 及以上

## 快速开始

### 1. 启动后端

```bash
mvn spring-boot:run
```

应用启动后默认监听 `8080` 端口。

> 项目已移除外部 MySQL 与 Redis 依赖，开发、测试、生产均使用内嵌 H2 数据库（MySQL 兼容模式）。
>
> - 开发环境：H2 文件库，数据持久化到 `./data/molly-dev`
> - 测试环境：H2 内存库 `molly-test`，每次测试独立初始化
> - 生产环境：H2 文件库，数据持久化到 `./data/molly-prod`
>
> 表结构与基础数据由 `sql/V1__init_schema.sql`、`sql/V2__init_data.sql` 提供，应用启动时通过 Flyway 自动迁移。
>
> 后续对表结构或基础数据的变更，按 `sql/V{版本号}__描述.sql` 命名新增迁移脚本，Flyway 会在启动时自动按版本顺序执行。

默认超级管理员账号：

- 用户名：`admin`
- 密码：`admin123`

### 2. 访问系统

打开浏览器访问 `http://localhost:8080`，会自动跳转到登录页。

## 页面列表

| 页面 | 路径 |
| --- | --- |
| 登录 | `/login` |
| 仪表盘 | `/` 或 `/dashboard` |
| 用户管理 | `/users` |
| 角色管理 | `/roles` |
| 权限管理 | `/permissions` |
| 登录日志 | `/login-logs` |
| 操作日志 | `/operation-logs` |

## 主要接口

| 接口 | 说明 |
| --- | --- |
| `POST /api/auth/login` | 登录（Session/Cookie） |
| `POST /api/auth/logout` | 登出 |
| `GET /api/auth/info` | 获取当前登录用户信息、角色、权限与菜单 |
| `GET /api/users` | 用户列表 |
| `GET /api/roles` | 角色列表 |
| `GET /api/permissions` | 权限树 |
| `GET /api/logs/login` | 登录日志 |
| `GET /api/logs/operation` | 操作日志 |

## 常用命令

### 后端

| 命令 | 说明 |
| --- | --- |
| `mvn clean` | 清理构建产物 |
| `mvn compile` | 编译主代码 |
| `mvn test` | 运行测试 |
| `mvn package` | 打包（默认生成可执行 jar） |
| `mvn spring-boot:run` | 本地启动应用 |

## 目录结构

```
molly
├── src/
│   ├── main/
│   │   ├── java/com/demo/molly/
│   │   │   ├── aspect/         # 操作日志 AOP
│   │   │   ├── common/         # 通用结果封装与错误码
│   │   │   ├── config/         # 配置类
│   │   │   ├── controller/     # 控制器
│   │   │   ├── dto/            # 请求参数
│   │   │   ├── entity/         # 实体类
│   │   │   ├── exception/      # 异常处理
│   │   │   ├── mapper/         # MyBatis Mapper
│   │   │   ├── security/       # Spring Security + Session
│   │   │   ├── service/        # 业务逻辑
│   │   │   ├── util/           # 工具类
│   │   │   ├── vo/             # 视图对象
│   │   │   └── MollyApplication.java
│   │   └── resources/
│   │       ├── mapper/         # MyBatis XML
│   │       ├── static/         # CSS、JS、图标等静态资源
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── favicon.svg
│   │       ├── templates/      # Thymeleaf 页面模板
│   │       │   ├── layout.html
│   │       │   ├── login.html
│   │       │   ├── dashboard.html
│   │       │   ├── users.html
│   │       │   ├── roles.html
│   │       │   ├── permissions.html
│   │       │   ├── login-logs.html
│   │       │   └── operation-logs.html
│   │       ├── application.yml              # 公共配置
│   │       ├── application-dev.yml          # 开发环境配置
│   │       └── application-prod.yml         # 生产环境配置
│   └── test/
│       ├── java/com/demo/molly/            # 测试类
│       │   ├── service/
│       │   │   └── TokenCacheServiceTest.java
│       │   └── MollyApplicationTests.java
│       └── resources/
│           ├── application.yml             # 测试默认激活 test profile
│           └── application-test.yml        # 测试环境数据源（H2 内存库）
├── sql/
│   ├── V1__init_schema.sql                  # Flyway 建表脚本
│   ├── V2__init_data.sql                    # Flyway 初始化基础数据
│   └── V3__update_permission_codes.sql      # Flyway 权限编码迁移示例
├── pom.xml
└── README.md
```

## 部署

```bash
mvn clean package -DskipTests
java -jar target/molly-0.0.1-SNAPSHOT.jar
```

生产环境默认使用 H2 文件库（`./data/molly-prod`），启动后会自动初始化表结构和基础数据。

## 许可证

本项目仅作示例用途，如需开源发布请补充合适的 License。
