# molly

基于 Spring Boot 4.0 的后端服务示例项目，集成 Spring Web、Spring Security、MyBatis 与 MySQL。

## 项目信息

| 项目 | 说明 |
| --- | --- |
| 项目名 | molly |
| 包名 | `com.demo.molly` |
| 构建工具 | Maven |
| Spring Boot | 4.0.0 |
| Java | 21 |
| 数据库 | MySQL |

## 技术栈

- Spring Boot 4.0.0
- Spring Web
- Spring Security
- MyBatis（mybatis-spring-boot-starter）
- MySQL Connector/J

## 环境要求

- JDK 21 及以上
- Maven 3.9 及以上
- MySQL 8.0 及以上（可按需调整驱动版本）

建议使用以下开发工具：

- IntelliJ IDEA / Eclipse / VS Code
- Git

## 快速开始

### 1. 克隆代码

```bash
git clone https://github.com/GeBron/molly.git
cd molly
```

### 2. 初始化数据库

在 MySQL 中创建数据库（默认数据库名：`molly`）：

```sql
CREATE DATABASE molly DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

> 如果不需要立即连接数据库，可暂时跳过此步骤，启动前在 `application.properties` 中配置相关参数。

### 3. 配置数据源

编辑 `src/main/resources/application.properties`，追加以下内容（请按本地环境修改）：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/molly?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password
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
