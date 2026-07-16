# molly

Molly 后台管理系统（非前后端分离版本），基于 Spring Boot 服务端托管的 jQuery + Bootstrap 多页面应用，使用 Spring Security Session/Cookie 登录鉴权。

## 项目信息

| 项目 | 说明 |
| --- | --- |
| 项目名 | molly |
| 包名 | `com.demo.molly` |
| 构建工具 | Maven |
| Spring Boot | 2.7.18 |
| Java | 1.8 |
| 数据库 | TiDB Cloud（MySQL 兼容）/ MySQL |
| 缓存 | Redis |
| 前端 | jQuery + Bootstrap 5 + DataTables + jsTree + flatpickr（CDN） |

## 技术栈

### 后端

- Java 1.8
- Spring Boot 2.7.18
- Spring Web
- Spring Security（Session/Cookie 登录）
- Spring Validation
- Spring AOP
- Spring Data Redis
- MyBatis Spring Boot Starter 2.3.2
- MySQL Connector/J

### 前端

- jQuery 3.7.1
- Bootstrap 5.3.2
- Bootstrap Icons 1.11.1
- DataTables 1.13.8（表格分页）
- jsTree 3.3.16（权限树）
- flatpickr 4.6.13（日期时间范围）

所有前端依赖均通过 CDN 引入。

## 环境要求

- JDK 1.8 及以上
- Maven 3.6 及以上
- Redis 服务（本地或远程，默认 `localhost:6379`）
- TiDB Cloud / MySQL 数据库

## 快速开始

### 1. 配置环境变量

```bash
export TiDB_USERNAME=<your_tidb_username>
export TiDB_PASSWORD=<your_tidb_password>
export REDIS_USERNAME=<your_redis_username>
export REDIS_PASSWORD=<your_redis_password>
```

> 测试环境默认使用独立的 `test` 数据库，避免与开发/生产库 `molly` 互相影响。运行测试前请确保 TiDB/MySQL 中已创建 `test` 数据库，并手动导入表结构和基础数据。
>
> 当前 Redis 连接信息（host、port、ssl、database）在 `application.yml` 中硬编码，仅 username/password 通过环境变量注入。

### 2. 初始化数据库

项目已移除 Flyway，数据库表结构和基础数据需要手动导入。请先在 TiDB/MySQL 中创建对应的数据库（开发/生产使用 `molly`，测试使用 `test`），然后执行以下 SQL 文件：

- `sql/init_schema.sql`：建表语句
- `sql/init_data.sql`：初始化基础数据（含默认管理员账号）

例如使用 MySQL 客户端：

```bash
mysql -h <host> -P 4000 -u <username> -p molly < sql/init_schema.sql
mysql -h <host> -P 4000 -u <username> -p molly < sql/init_data.sql
```

### 3. 启动后端

```bash
mvn spring-boot:run
```

应用启动后默认监听 `8080` 端口。

默认超级管理员账号：

- 用户名：`admin`
- 密码：`admin123`

### 3. 访问系统

打开浏览器访问 `http://localhost:8080`，会自动跳转到登录页。

## 页面列表

| 页面 | 路径 |
| --- | --- |
| 登录 | `/login.html` |
| 仪表盘 | `/dashboard.html` |
| 用户管理 | `/users.html` |
| 角色管理 | `/roles.html` |
| 权限管理 | `/permissions.html` |
| 登录日志 | `/login-logs.html` |
| 操作日志 | `/operation-logs.html` |

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
│   │       ├── static/         # jQuery + Bootstrap 前端静态资源
│   │       │   ├── css/
│   │       │   ├── js/
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
│       │   │   └── RedisServiceTest.java
│       │   └── MollyApplicationTests.java
│       └── resources/
│           ├── application.yml             # 测试默认激活 test profile
│           └── application-test.yml        # 测试环境数据源（test 库）
├── sql/
│   ├── init_schema.sql                      # 建表语句
│   └── init_data.sql                        # 初始化基础数据
├── pom.xml
└── README.md
```

## 部署

```bash
mvn clean package -DskipTests
java -jar target/molly-0.0.1-SNAPSHOT.jar
```

生产环境建议通过环境变量注入数据库账号、Redis 密码等敏感信息。

## 许可证

本项目仅作示例用途，如需开源发布请补充合适的 License。
