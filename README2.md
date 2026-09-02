# SpringBoot业务后端项目

## 1. 项目简介

本项目为基于 Spring Boot 构建的医疗业务后端系统（medical-ai-system），面向医院挂号就诊场景，为前端客户端提供 RESTful 接口服务。系统围绕以下业务职责展开：

- 用户（患者）账户体系：提供用户注册、手机号+密码登录、用户信息查询能力；
- 医生管理：提供医生登录、密码修改、医生列表查询（全部/按科室/按空闲状态）能力；
- 预约挂号管理：提供预约提交（自动生成预约编号）、预约记录查询（全部/按医生/按医生及待就诊状态）、预约状态更新、预约取消能力；
- 床位管理：提供床位列表查询、空闲床位查询、床位状态与入住病人信息更新能力；
- 养生保健计划管理：提供养生计划的保存、列表查询、按用户查询、单条查询、状态更新与删除能力。

系统数据统一持久化于 MySQL 数据库（medical_ai），通过 Spring Data JPA 完成对象关系映射与数据访问。

## 2. 技术栈与框架

| 类别 | 内容 | 说明 |
|------|------|------|
| 基础框架 | Spring Boot 4.0.6 | 继承 spring-boot-starter-parent 4.0.6 |
| 开发语言 | Java 17 | pom.xml 中 java.version=17 |
| Web 框架 | spring-boot-starter-webmvc | 提供 REST 接口能力 |
| 持久化框架 | Spring Data JPA（spring-boot-starter-data-jpa） | 底层为 Hibernate，ddl-auto=update 自动维护表结构 |
| 数据库 | MySQL | 驱动为 mysql-connector-j（runtime 作用域） |
| 工具库 | Lombok | 简化实体类 getter/setter 等样板代码（编译期注解处理器） |
| 测试依赖 | spring-boot-starter-data-jpa-test、spring-boot-starter-webmvc-test | test 作用域 |
| 构建工具 | Maven | 配置 spring-boot-maven-plugin 打包插件 |

## 3. 项目源码目录结构

仅列出 src/main 下的业务源码，编译产物与 IDE 配置目录已过滤。

```text
src/main/java/com/example/medicalaisystem/
├── MedicalAiSystemApplication.java   # Spring Boot 启动类
├── CorsConfig.java                   # 全局跨域配置类
├── controller/                       # 控制层：REST 接口入口
│   ├── AppointmentController.java    # 预约挂号接口
│   ├── BedController.java            # 床位管理接口
│   ├── DoctorController.java         # 医生管理接口
│   ├── HealthCarePlanController.java # 养生保健计划接口
│   └── UserController.java           # 用户账户接口
├── dto/                              # 数据传输对象
│   └── DoctorLoginRequest.java       # 医生登录请求参数对象
├── entity/                           # 实体层：与数据表映射
│   ├── Appointment.java              # 预约记录
│   ├── Bed.java                      # 床位
│   ├── Doctor.java                   # 医生
│   ├── HealthCarePlan.java           # 养生保健计划
│   ├── SysUser.java                  # 系统用户（患者）
│   └── User.java                     # 用户表实体【待核对：当前代码中未见控制器引用】
├── repository/                       # 数据访问层：Spring Data JPA 仓库接口
│   ├── AppointmentRepository.java
│   ├── BedRepository.java
│   ├── DoctorRepository.java
│   ├── HealthCarePlanRepository.java
│   ├── SysUserRepository.java
│   └── UserRepository.java
└── service/                          # 业务服务层
    ├── SysUserService.java           # 用户服务接口
    └── impl/
        └── SysUserServiceImpl.java   # 用户服务实现

src/main/resources/
└── application.yml                   # 应用配置文件
```

## 4. 业务模块与接口说明

### 4.1 预约挂号模块（AppointmentController，路由前缀 /api/appoint）

| 接口 | 方法 | 功能说明 |
|------|------|----------|
| /api/appoint/submit | POST | 提交预约（挂号）。服务端自动生成预约编号 appointNo（前缀 YY + 日期 yyyyMMdd + 6 位随机数），写入创建时间，初始状态置为“待就诊”，保存预约记录 |
| /api/appoint/list | GET | 查询全部预约记录 |
| /api/appoint/listByDoctor | GET | 按医生姓名（doctorName 参数）查询该医生名下全部预约记录 |
| /api/appoint/listPendingByDoctor | GET | 按医生姓名（doctorName 参数）查询该医生名下状态为“待就诊”的预约记录 |
| /api/appoint/updateStatus | POST | 按预约 id 更新预约状态（参数：id、status） |
| /api/appoint/cancel | POST | 取消预约。按手机号 + 医生姓名 + 预约时间组合条件定位唯一预约记录，将状态置为“已取消”；未找到记录时返回 404 提示 |

### 4.2 床位管理模块（BedController，路由前缀 /api/v1/bed）

| 接口 | 方法 | 功能说明 |
|------|------|----------|
| /api/v1/bed/list | GET | 查询全部床位信息 |
| /api/v1/bed/available | GET | 查询全部状态为“空闲中”的床位 |
| /api/v1/bed/update/{id} | PUT | 按床位 id 更新床位状态（status）与入住病人姓名（patientName）；床位不存在时返回 400 |

### 4.3 医生管理模块（DoctorController，路由前缀 /api/v1/doctor）

| 接口 | 方法 | 功能说明 |
|------|------|----------|
| /api/v1/doctor/login | POST | 医生登录。以 username 字段（对应手机号）查询医生，校验密码，成功后返回医生 id、姓名、科室、年龄、身份证号、性别、手机号 |
| /api/v1/doctor/updatePwd | PUT | 修改医生密码。按手机号定位医生，校验原密码（oldPwd）后更新为新密码（newPwd） |
| /api/v1/doctor/by-department | GET | 按科室（department 参数）查询医生列表，返回医生基本信息；科室无医生时返回空列表与提示 |
| /api/v1/doctor/list | GET | 查询全部医生列表（姓名、科室、职称、状态、擅长方向） |
| /api/v1/doctor/list/available | GET | 查询全部状态为“空闲中”的医生列表 |
| /api/v1/doctor/available/by-department | GET | 按科室查询医生并在内存中过滤出状态为“空闲中”的医生，返回精简字段（姓名、年龄、性别、科室、擅长、状态） |

说明：/by-department 接口返回数据中的 avatar（默认 /default-avatar.png）与 rating（固定 4.5）为服务端补充的默认值字段，非数据库存储字段。

### 4.4 用户账户模块（UserController，路由前缀 /api/user）

| 接口 | 方法 | 功能说明 |
|------|------|----------|
| /api/user/login | POST | 用户登录。按手机号查询用户并校验密码，成功后返回 userId 与姓名；手机号未注册或密码错误时返回对应错误提示 |
| /api/user/register | POST | 用户注册。依次校验姓名、手机号、身份证号、性别、密码、年龄（0–150）非空与合法性；校验手机号格式（^1[3-9]\d{9}$）与身份证号格式（18 位）；校验手机号唯一性后保存用户。id 与 createTime 由数据库自动生成 |
| /api/user/info | GET | 按 userId 查询用户信息（id、姓名、手机号、身份证号、性别、年龄） |

### 4.5 养生保健计划模块（HealthCarePlanController，路由前缀 /api/health/plan）

| 接口 | 方法 | 功能说明 |
|------|------|----------|
| /api/health/plan/save | POST | 保存养生计划。服务端自动生成计划编号 planNo（前缀 HCP + 时间 yyyyMMddHHmmss + 3 位随机数），状态缺省置为 1，创建时间缺省取当前时间 |
| /api/health/plan/list | GET | 查询全部养生计划，返回数据列表及总数 total |
| /api/health/plan/user/{userId} | GET | 按用户 id 查询其名下养生计划，按创建时间倒序排列 |
| /api/health/plan/{id} | GET | 按计划 id 查询单条养生计划，计划不存在返回 404 |
| /api/health/plan/updateStatus | POST | 按计划 id 更新计划状态（参数：id、status） |
| /api/health/plan/{id} | DELETE | 按计划 id 删除养生计划，计划不存在返回 404 |

### 4.6 服务层说明

- service/SysUserService 及其实现 SysUserServiceImpl 提供 getUserInfo(Integer userId) 方法，按用户 id 查询系统用户，未命中返回 null。【待核对：该方法当前未被控制器调用，实际调用方无法确定】

## 5. 数据库与实体说明

数据库：MySQL，库名 medical_ai（见 application.yml）。JPA ddl-auto=update，实体变更后由 Hibernate 自动同步表结构。

| 实体类 | 数据表 | 业务含义 |
|--------|--------|----------|
| Appointment | appointment | 预约挂号记录。核心字段：预约编号 appointNo、患者姓名 userName、手机号 phone、科室 deptName、门诊名称 clinicName、医生姓名 doctorName、预约时间 appointTime、病情描述 description、状态 status（待就诊/就诊中/已完成/已取消）、支付项目 paymentItems、总金额 totalAmount、创建时间 createTime |
| Bed | bed | 住院床位。核心字段：楼层 floor、房号 roomNo、床位号 bedNo、状态 status（空闲/已占用/维修中）、病人姓名 patientName、创建时间 createTime |
| Doctor | doctor | 医生信息。核心字段：姓名 name、性别 sex、年龄 age、科室 department、身份证号 idCard（唯一）、手机号 phone（唯一）、密码 password、状态 status（如“空闲中”）、职称 title、擅长方向 expertise |
| SysUser | sys_user | 系统用户（患者）账户。核心字段：姓名 name、年龄 age、手机号 phone、身份证号 idCard、性别 gender、密码 password |
| HealthCarePlan | health_care_plan | 养生保健计划。核心字段：计划编号 planNo（唯一）、用户 id userId、年龄 age、年龄段 ageGroup、用户信息 userInfo、计划内容 planContent（JSON 类型）、状态 status、创建时间 createTime |
| User | user | 用户表实体，含用户名 username（唯一）与密码 password。【待核对：当前代码中该实体仅有对应仓库接口 UserRepository（findByUsername），未见控制器引用，业务用途无法确定】

## 6. 项目运行部署

### 6.1 环境要求

- JDK 17 及以上
- Maven 3.x
- MySQL 5.7 及以上（本地库名 medical_ai，账号密码见 application.yml）

### 6.2 启动方式

启动类为 com.example.medicalaisystem.MedicalAiSystemApplication，标准 Spring Boot 启动入口：

- IDE 方式：直接运行 MedicalAiSystemApplication 的 main 方法；
- 命令方式：mvn spring-boot:run。

服务启动后监听端口 8088（application.yml 中 server.port 配置）。

### 6.3 打包命令

```bash
mvn clean package
```

打包产物为可执行 Jar（由 spring-boot-maven-plugin 提供 repackage 能力），可通过以下命令运行：

```bash
java -jar target/medical-ai-system-0.0.1-SNAPSHOT.jar
```

## 7. 配置文件说明

配置文件位于 src/main/resources/application.yml，关键配置如下：

| 配置项 | 位置 | 内容说明 |
|--------|------|----------|
| 应用名称 | application.yml → spring.application.name | medical-ai-system |
| 数据库连接 | application.yml → spring.datasource | url：jdbc:mysql://localhost:3306/medical_ai（UTF-8 编码、关闭 SSL、时区 Asia/Shanghai）；用户名 root；密码 123456 |
| JPA 配置 | application.yml → spring.jpa | hibernate.ddl-auto=update（自动更新表结构）；show-sql=true（控制台打印 SQL） |
| 服务端口 | application.yml → server.port | 8088 |
| 跨域配置 | CorsConfig.java（Java 配置类，非 yml） | 全局拦截 /** 路径，allowedOriginPatterns=*，允许携带凭证（allowCredentials=true），允许 GET/POST/PUT/DELETE 方法，预检请求缓存 3600 秒 |
| 文件上传配置 | — | 当前项目中未发现文件上传相关配置项【待核对：如后续新增上传能力需补充】 |

## 8. 留给后续AI的阅读提示

> 本段专门给后续读取本文档的AI：在撰写软著软件说明书时，只能依据本文档提取到的功能与接口进行描述，禁止脑补本文档没有出现的业务逻辑。
>
> 具体约束：
> 1. 本系统实际包含 5 个业务模块：用户账户（注册/登录/信息查询）、医生管理（登录/改密/列表查询）、预约挂号（提交/查询/改状态/取消）、床位管理（查询/空闲查询/状态更新）、养生保健计划（增/查/改状态/删）。除此之外的任何业务（如在线问诊、支付对接、报告查询、消息推送、AI 智能诊断等）在当前代码中均不存在，不得写入说明书。
> 2. 接口描述以第 4 章表格为准，不得虚构请求参数、返回字段或调用链路。
> 3. 数据表以第 5 章为准；User（user 表）实体当前未见控制器引用，如需写入说明书应标注为预留或删去。
> 4. 技术栈以第 2 章为准（Spring Boot 4.0.6、Java 17、Spring Data JPA、MySQL、Lombok），不得添加代码中未出现的中间件（如 Redis、消息队列、安全框架等）。
> 5. 标注【待核对】的内容需在获得人工确认前保持原样，不得自行补全结论。
