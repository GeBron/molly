# molly

Molly Spring Boot 4.0 Project

## 技术栈

- Java 17
- Spring Boot 4.0.7
- Spring Security
- MyBatis Spring Boot Starter 4.0.1
- MySQL
基于 Spring Boot 4.0 的后端服务示例项目，集成 Spring Web、Spring Security、MyBatis 与 MySQL（数据库使用 TiDB Cloud 托管的 MySQL 兼容实例）。

## 项目信息

| 项目 | 说明 |
| --- | --- |
| 项目名 | molly |
| 包名 | `com.demo.molly` |
| 构建工具 | Maven |
| Spring Boot | 4.0.0 |
| Java | 21 |
| 数据库 | TiDB Cloud（MySQL 兼容） |

## 技术栈

- Spring Boot 4.0.0
- Spring Web
- Spring Security
- MyBatis（mybatis-spring-boot-starter）
- MySQL Connector/J

## 环境要求

- JDK 21 及以上
- Maven 3.9 及以上
- TiDB Cloud 账号（项目已使用位于 `ap-southeast-1` 区域的 TiDB Cloud 集群，地址：`gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000`）

建议使用以下开发工具：

- IntelliJ IDEA / Eclipse / VS Code
- Git

## 快速开始

### 1. 克隆代码

```bash
git clone https://github.com/GeBron/molly.git
cd molly
```

### 2. 配置 TiDB Cloud 凭据

项目数据源指向 TiDB Cloud 托管的 MySQL 兼容实例，无需本地安装 MySQL。连接信息以环境变量方式注入，启动前需在系统中设置：

```bash
export TiDB_USERNAME=<your_tidb_username>
export TiDB_PASSWORD=<your_tidb_password>
```

> 请在 TiDB Cloud 控制台创建集群与数据库 `molly`，并确保当前网络可访问 `gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000`（TiDB Cloud 默认要求 TLS 加密连接）。

### 3. 配置数据源

`src/main/resources/application.properties` 中已使用上述环境变量配置数据源，无需手动修改：

```properties
spring.datasource.url=jdbc:mysql://${TiDB_USERNAME}:${TiDB_PASSWORD}@gateway01.ap-southeast-1.prod.alicloud.tidbcloud.com:4000/molly?sslMode=VERIFY_IDENTITY
spring.datasource.username=${TiDB_USERNAME}
spring.datasource.password=${TiDB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 4. 构建并运行

使用 Maven 直接运行：

```bash
cd molly
mvn spring-boot:run
```

或者先打包再运行：

```bash
mvn clean package -DskipTests
java -jar target/molly-0.0.1-SNAPSHOT.jar
```

应用启动后默认监听 `8080` 端口。

## 开发步骤

1. 在 `src/main/java/com/demo/molly` 下编写业务代码（建议按 `controller / service / mapper / entity / config` 分包）。
2. MyBatis 映射文件可放置在 `src/main/resources/mapper` 目录下，并在 `application.properties` 中配置 `mybatis.mapper-locations`。
3. Spring Security 相关配置建议放置在 `config` 包下，例如 `SecurityConfig`。
4. 单元测试写在 `src/test/java/com/demo/molly` 下，运行 `mvn test` 触发测试。

## 常用命令

| 命令 | 说明 |
| --- | --- |
| `mvn clean` | 清理构建产物 |
| `mvn compile` | 编译主代码 |
| `mvn test` | 运行测试 |
| `mvn package` | 打包（默认生成可执行 jar） |
| `mvn spring-boot:run` | 本地启动应用 |

## 部署

### 本地部署

```bash
mvn clean package -DskipTests
java -jar target/molly-0.0.1-SNAPSHOT.jar
```

### 服务器部署建议

1. 将 `target/molly-0.0.1-SNAPSHOT.jar` 上传至服务器。
2. 使用 `nohup` 或 `systemd` 托管进程，例如：

```bash
nohup java -jar molly-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
```

3. 生产环境建议通过环境变量或外部配置中心注入数据库账号、密钥等敏感信息，避免硬编码在配置文件中。

## 目录结构

```
molly
├── pom.xml
└── src
    ├── main
    │   ├── java/com/demo/molly/MollyApplication.java
    │   └── resources/application.properties
    └── test
        └── java/com/demo/molly/MollyApplicationTests.java
```

## 许可证

本项目仅作示例用途，如需开源发布请补充合适的 License。
