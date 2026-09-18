<<<<<<< HEAD
# AI智能问诊与养生系统 — 后端模块

## 1. 模块简介

本模块是 **AI智能问诊与养生系统 V1.0** 的后端服务，基于 SpringBoot + MyBatis-Plus 构建，提供以下业务能力：

- **用户管理**：注册、登录、个人信息查询
- **医生管理**：登录、密码修改、按科室查询医生列表
- **挂号管理**：提交挂号、查询列表、更新就诊状态
- **床位管理**：查询所有床位、更新床位状态与病人绑定
- **养生计划管理**：养生计划增删改查、按用户查询、状态更新

## 2. 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.x | 应用框架 |
| MyBatis-Plus | 3.5.x | ORM 持久层框架 |
| Maven | 3.8+ | 项目构建与依赖管理 |
| MySQL | 8.0+ | 关系型数据库 |
| JDK | 17 | Java 运行环境 |
| Lombok | 1.18.x | 代码简化（Getter/Setter） |

## 3. 前置环境

> 团队所有成员必须统一以下版本，避免环境不一致导致的问题。

| 环境 | 要求版本 | 检查命令 |
|------|----------|----------|
| JDK | **17**（精确到小版本无要求，必须为 17） | `java -version` |
| Maven | 3.8+ | `mvn -v` |
| MySQL | 8.0+ | `mysql --version` |
| Git | 任意 | `git --version` |

### 安装指引

- **JDK 17**：https://adoptium.net/temurin/releases/?version=17
- **Maven**：https://maven.apache.org/download.cgi ，解压后配置 `MAVEN_HOME` 环境变量
- **MySQL 8.0+**：https://dev.mysql.com/downloads/installer/

## 4. 项目目录说明

```
medical-ai-system/
├── pom.xml                         # Maven 项目依赖配置（核心，提交到仓库）
├── src/main/java/com/example/medicalaisystem/
│   ├── MedicalAiSystemApplication.java   # 启动类
│   ├── CorsConfig.java                   # 跨域配置
│   ├── controller/                       # Controller 层（API 接口）
│   │   ├── UserController.java
│   │   ├── DoctorController.java
│   │   ├── AppointmentController.java
│   │   ├── BedController.java
│   │   └── HealthCarePlanController.java
│   ├── service/                          # Service 层（业务逻辑接口 + 实现）
│   │   ├── SysUserService.java
│   │   └── impl/SysUserServiceImpl.java
│   ├── mapper/                           # Mapper 层（MyBatis-Plus 数据访问）
│   ├── entity/                           # Entity 层（数据库表实体映射）
│   │   ├── SysUser.java
│   │   ├── Doctor.java
│   │   ├── Appointment.java
│   │   ├── Bed.java
│   │   ├── HealthCarePlan.java
│   │   └── User.java
│   ├── dto/                              # DTO（数据传输对象）
│   │   └── DoctorLoginRequest.java
│   └── config/                           # 配置类
├── src/main/resources/
│   ├── application.yml                   # 本地配置文件（已 .gitignore，不提交）
│   ├── application-example.yml           # 配置模板（提交到仓库，队友复制改密码）
│   └── mapper/                           # MyBatis XML 映射文件
└── src/test/java/                        # 单元测试
```

## 5. 🚀 完整启动步骤

### 步骤 1：克隆仓库

```bash
git clone https://atomgit.com/xxx/ai-medical-system.git
cd ai-medical-system
```

> 仓库为 AtomGit 私有团队仓库，请确保已添加 SSH Key 或配置 HTTP 凭据。如无权限，联系管理员添加仓库成员。

### 步骤 2：Maven 依赖加载

```bash
# 在项目根目录执行（第一次需要下载依赖，耗时取决于网络）
mvn clean install -DskipTests
```

**注意事项：**
- 首次执行会从 Maven 中央仓库下载所有依赖，请确保网络畅通
- 如下载失败，尝试更换阿里云镜像（见 FAQ）
- Maven Wrapper 已内置（`mvnw` / `mvnw.cmd`），如未安装 Maven 可使用：

```bash
# Windows
mvnw.cmd clean install -DskipTests

# macOS / Linux
./mvnw clean install -DskipTests
```

### 步骤 3：配置文件处理

**不要直接编辑 `application.yml`**，该文件已加入 `.gitignore` 不会被提交。请按以下步骤操作：

```bash
# 1. 复制配置模板
cp src/main/resources/application-example.yml src/main/resources/application.yml

# 2. 编辑 application.yml，修改为本机数据库账号密码
```

配置模板内容如下（`application-example.yml`）：

```yaml
spring:
  application:
    name: medical-ai-system
  datasource:
    url: jdbc:mysql://localhost:3306/medical_ai?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root                    # 改为你的 MySQL 用户名
    password: 123456                  # 改为你的 MySQL 密码
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Shanghai

mybatis-plus:
  mapper-locations: classpath:/mapper/*.xml
  type-aliases-package: com.example.medicalaisystem.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

server:
  port: 8088
```

### 步骤 4：导入数据库脚本

```bash
# 登录 MySQL
mysql -u root -p

# 创建数据库
CREATE DATABASE IF NOT EXISTS medical_ai DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 退出后导入表结构脚本
mysql -u root -p medical_ai < sql/init.sql
```

> `sql/init.sql` 文件位于项目根目录 `sql/` 下，包含所有表结构定义。如尚未创建该文件，可通过 MyBatis-Plus 的 `ddl-auto` 或手动执行建表语句。表清单如下：

| 表名 | 说明 |
|------|------|
| `sys_user` | 系统用户表 |
| `doctor` | 医生表 |
| `appointment` | 挂号表 |
| `bed` | 床位表 |
| `health_care_plan` | 养生计划表 |
| `user` | 基础用户表 |

### 步骤 5：启动项目

```bash
# 方式一：IDE 启动
# 打开 IDEA → 打开项目 → 等待索引完成 → 运行 MedicalAiSystemApplication.java

# 方式二：命令行启动
mvn spring-boot:run
```

启动成功后，控制台输出类似：

```
Started MedicalAiSystemApplication in 3.2 seconds
```

**服务访问地址：** `http://localhost:8088`

## 6. 配置说明

`application-example.yml` 各项配置含义：

| 配置项 | 说明 | 必改 |
|--------|------|------|
| `spring.datasource.url` | MySQL 连接地址，`localhost:3306` 为本机默认端口，`medical_ai` 为数据库名 | 如数据库名或主机不同则需改 |
| `spring.datasource.username` | MySQL 登录用户名，默认 `root` | **是** |
| `spring.datasource.password` | MySQL 登录密码 | **是** |
| `spring.jackson.date-format` | JSON 序列化日期格式 | 否 |
| `spring.jackson.time-zone` | 时区设置 | 否 |
| `mybatis-plus.mapper-locations` | MyBatis XML 映射文件路径 | 否 |
| `mybatis-plus.type-aliases-package` | 实体类包路径，用于 XML 中简写 | 否 |
| `mybatis-plus.configuration.map-underscore-to-camel-case` | 下划线自动转驼峰（`user_name` → `userName`） | 否 |
| `mybatis-plus.configuration.log-impl` | SQL 日志输出实现，开发阶段开启，生产环境可关闭 | 否 |
| `server.port` | 服务端口号，默认 `8088` | 如端口冲突则改 |

## 7. Git 团队协作规范

### 禁止提交的文件

以下文件已加入 `.gitignore`，切勿手动 `git add` 或 `git commit`：

| 文件/目录 | 原因 |
|-----------|------|
| `target/` | Maven 编译输出目录，每个人本地生成 |
| `application.yml` | 包含本机数据库密码，严禁提交 |
| `.idea/` | IDEA 编辑器配置，因人而异 |
| `*.iml` | IDEA 模块配置 |
| `.vscode/` | VS Code 配置 |
| `*.log` | 运行时日志文件 |

### 依赖管理规范

- `pom.xml` 是核心依赖文件，**必须提交到仓库**
- 队友本地执行 `mvn clean install` 自动从远程仓库拉取 jar，**不需要上传本地 jar 包**
- 新增依赖统一在 `pom.xml` 中添加，并通知团队其他成员 `git pull` 后重新 `mvn install`

### 提交流程

```bash
# 1. 查看变更
git status

# 2. 添加需要提交的文件（逐个添加，不要 git add . 以免误提交敏感文件）
git add src/main/java/com/example/medicalaisystem/controller/UserController.java
git add src/main/resources/application-example.yml

# 3. 提交
git commit -m "feat: 用户模块新增年龄字段"

# 4. 推送到远程
git push origin dev
```

### 分支约定

- `main`：稳定版本，代码需经 review 后合并
- `dev`：开发分支，日常提交到此处
- `feature/xxx`：功能分支，开发完成后合并到 `dev`

## 8. FAQ 常见问题

### Q1: JDK 版本不匹配

**错误信息：**
```
java: invalid source release 17
```

**排查方案：**
```bash
# 检查 JDK 版本
java -version

# 确保 IDEA 中 Project Structure → Project SDK 选择 JDK 17
# File → Project Structure → Project → SDK → 选择 17
# File → Settings → Build → Compiler → Java Compiler → Target bytecode version → 17
```

### Q2: Maven 依赖下载失败

**错误信息：**
```
Could not transfer artifact xxx from/to central
```

**排查方案：**

1. 检查网络连接，确认能访问 `https://repo.maven.apache.org/`
2. 配置阿里云镜像（修改 `~/.m2/settings.xml`）：

```xml
<mirror>
    <id>aliyunmaven</id>
    <mirrorOf>central</mirrorOf>
    <name>阿里云公共仓库</name>
    <url>https://maven.aliyun.com/repository/public</url>
</mirror>
```

3. 重试：

```bash
mvn clean install -DskipTests -U
```

### Q3: MySQL 连接报错

**错误信息：**
```
Access denied for user 'root'@'localhost'
Cannot create PoolableConnectionFactory
```

**排查方案：**
```bash
# 1. 确认 MySQL 服务正在运行
# Windows: 任务管理器 → 服务 → MySQL80
# macOS/Linux: ps aux | grep mysql

# 2. 确认 application.yml 中用户名和密码正确
# 3. 尝试命令行登录验证
mysql -u root -p

# 4. 如密码忘记，重置 root 密码
# 5. 确认数据库 medical_ai 已创建
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS medical_ai DEFAULT CHARACTER SET utf8mb4;"
```

### Q4: 端口占用

**错误信息：**
```
Web server failed to start. Port 8088 was already in use.
```

**排查方案：**
```bash
# 查看 8088 端口被哪个进程占用
# Windows
netstat -ano | findstr :8088
taskkill /PID 进程ID /F

# macOS / Linux
lsof -i :8088
kill -9 进程ID

# 或修改 application.yml 中的 server.port 为其他端口
```

### Q5: MyBatis 映射异常

**错误信息：**
```
Invalid bound statement (not found): com.example.mapper.xxxMapper.xxx
```

**排查方案：**
```bash
# 1. 确认 XML 文件路径是否正确
# 检查 application.yml 中 mybatis-plus.mapper-locations 配置
# 默认读取 classpath:/mapper/*.xml

# 2. 确认 XML 中 namespace 与 Mapper 接口全限定名一致
# 例如：Mapper 接口包为 com.example.medicalaisystem.mapper.SysUserMapper
# XML 中 namespace 需为 com.example.medicalaisystem.mapper.SysUserMapper

# 3. 确认 XML 中 id 与方法名一致

# 4. 清理重新编译
mvn clean compile -DskipTests
```

### Q6: Lombok 编译报错

**错误信息：**
```
java: cannot find symbol
symbol: method getXxx()
```

**排查方案：**
```bash
# 1. IDEA 安装 Lombok 插件
# File → Settings → Plugins → 搜索 Lombok → 安装

# 2. 开启注解处理
# File → Settings → Build → Compiler → Annotation Processors → Enable annotation processing

# 3. 确认 pom.xml 中 Lombok 依赖存在
```
=======
# AI-
【AI 智慧问诊医疗系统开发】项目简介 一、项目背景 随着基层医疗服务需求持续增长，县域门诊、社区卫生服务中心普遍面临就诊群体老龄化、方言沟通壁垒突出、听障与言语障碍群体就医交互不便等现实问题。同时，市面主流智慧问诊系统多采用云端集中部署模式，存在网络依赖强、数据安全风险高、基层场景适配成本高等短板，难以直接下沉至本土化医疗场景。针对上述痛点，本项目立项开展AI 智慧问诊医疗系统开发，打造本土化、轻量化、无障碍的基层智慧医疗服务体系。
>>>>>>> a92b855e5625799c3eb6572cc233bf7a04b9574e
