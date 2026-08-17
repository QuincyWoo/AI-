# 医疗AI管理系统

基于 Spring Boot 4.0.6 的医疗信息管理系统，提供医生登录、患者挂号、床位管理、养生计划管理等功能。

## 项目架构图

```
┌──────────────────────────────┐
│   API接口层 (Controller)     │   ← controller/
│   @RestController            │
├──────────────────────────────┤
│   业务逻辑层 (Service)       │   ← service/
│   接口 + 实现类               │
├──────────────────────────────┤
│   数据访问层 (Repository)    │   ← repository/
│   Spring Data JPA            │
├──────────────────────────────┤
│   数据库层 (MySQL)           │   ← medical_ai 数据库
│   JPA自动建表/更新            │
└──────────────────────────────┘
```

**各层关系说明：**

- **Controller 层**：接收 HTTP 请求，解析参数，调用 Service 层或 Repository 层处理业务逻辑，返回 JSON 响应。
- **Service 层**：封装核心业务逻辑，通过调用 Repository 完成数据操作。当前项目中部分 Controller 直接调用 Repository（简化设计）。
- **Repository 层**：继承 `JpaRepository`，提供 CRUD 操作及自定义查询方法（如 `findByPhone`、`findByDepartment`），由 Spring Data JPA 自动实现。
- **Entity 层**：使用 JPA 注解映射数据库表，定义实体结构与字段约束。
- **数据库层**：MySQL 存储所有业务数据，JPA 的 `ddl-auto: update` 策略会在应用启动时自动创建或更新表结构。

## 快速开始

### 1. 环境要求

| 依赖 | 版本要求 |
|------|----------|
| JDK  | 17+      |
| Maven| 3.6+     |
| MySQL| 8.0+     |

### 2. 安装依赖

```bash
mvn clean install
```

或使用 Maven Wrapper：

```bash
./mvnw clean install
```

### 3. 数据库配置

确保 MySQL 服务已运行，并创建数据库：

```sql
CREATE DATABASE medical_ai DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 [application.yml](file:///d:/java/medical-ai-system/src/main/resources/application.yml) 中的数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medical_ai?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: 123456
```

> 首次启动时，JPA 会根据 Entity 类自动创建表结构。

### 4. 启动应用

```bash
mvn spring-boot:run
```

或使用 Maven Wrapper：

```bash
./mvnw spring-boot:run
```

启动成功后，应用默认运行在 `http://localhost:8088`。

### 5. 运行测试

```bash
mvn test
```

## API 接口

> 所有接口均返回 JSON 格式，统一响应结构：`{ code, msg, data }`

### 用户模块 (`/api/user`)

| 接口 | 方法 | 请求参数 | 响应字段 | 说明 |
|------|------|----------|----------|------|
| `/api/user/login` | POST | Body: `phone`, `password` | `code`, `msg`, `userId`, `name` | 用户手机号+密码登录 |
| `/api/user/register` | POST | Body: `phone`, `password`, `name`, `idCard`, `gender` | `code`, `msg` | 用户注册（手机号不可重复） |
| `/api/user/info` | GET | Query: `userId` | `code`, `msg`, `data` (id, name, phone, idCard, gender) | 根据用户ID获取详细信息 |

### 医生模块 (`/api/v1/doctor`)

| 接口 | 方法 | 请求参数 | 响应字段 | 说明 |
|------|------|----------|----------|------|
| `/api/v1/doctor/login` | POST | Body: `username`(手机号), `password` | `code`, `msg`, `data` (doctorId, doctorName, department, age, sex, id_card, phone) | 医生手机号+密码登录 |
| `/api/v1/doctor/updatePwd` | PUT | Body: `phone`, `oldPwd`, `newPwd` | `code`, `msg` | 修改医生密码（需验证原密码） |
| `/api/v1/doctor/by-department` | GET | Query: `department` | `code`, `msg`, `data` (id, name, age, sex, department, phone, title, expertise, avatar, rating) | 根据科室获取医生列表 |

### 挂号模块 (`/api/appoint`)

| 接口 | 方法 | 请求参数 | 响应字段 | 说明 |
|------|------|----------|----------|------|
| `/api/appoint/submit` | POST | Body: `userName`, `phone`, `deptName`, `clinicName`, `doctorName`, `appointTime`, `description` | `code`, `msg` | 提交挂号（系统自动生成挂号号） |
| `/api/appoint/list` | GET | 无 | `code`, `data` (挂号列表) | 获取所有挂号记录 |
| `/api/appoint/updateStatus` | POST | Body: `id`, `status` | `code`, `msg` | 更新挂号状态（待就诊/就诊中/已完成） |

### 床位模块 (`/api/v1/bed`)

| 接口 | 方法 | 请求参数 | 响应字段 | 说明 |
|------|------|----------|----------|------|
| `/api/v1/bed/list` | GET | 无 | `code`, `data` (床位列表) | 获取所有床位信息 |
| `/api/v1/bed/update/{id}` | PUT | Path: `id`, Body: `status`, `patientName` | `code`, `msg` | 更新床位状态及绑定病人姓名 |

### 养生计划模块 (`/api/health/plan`)

| 接口 | 方法 | 请求参数 | 响应字段 | 说明 |
|------|------|----------|----------|------|
| `/api/health/plan/save` | POST | Body: `userId`, `age`, `ageGroup`, `userInfo`, `planContent`, `status`, `createTime` | `code`, `msg`, `data` (计划详情) | 保存养生计划（自动生成计划编号） |
| `/api/health/plan/list` | GET | 无 | `code`, `data` (计划列表), `total` | 查询所有养生计划 |
| `/api/health/plan/user/{userId}` | GET | Path: `userId` | `code`, `data` (计划列表), `total` | 根据用户ID查询计划（按创建时间倒序） |
| `/api/health/plan/{id}` | GET | Path: `id` | `code`, `data` (计划详情) | 根据ID查询单个计划 |
| `/api/health/plan/updateStatus` | POST | Body: `id`, `status` | `code`, `msg`, `data` (计划详情) | 更新计划状态 |
| `/api/health/plan/{id}` | DELETE | Path: `id` | `code`, `msg` | 删除养生计划 |

## 请求示例

### 用户注册

```json
POST /api/user/register
{
    "name": "张三",
    "phone": "13800138000",
    "idCard": "110101199001011234",
    "gender": "男",
    "password": "123456"
}
```

### 医生登录

```json
POST /api/v1/doctor/login
{
    "username": "13900139000",
    "password": "doctor123"
}
```

### 根据科室查询医生

```
GET /api/v1/doctor/by-department?department=呼吸内科
```

### 提交挂号

```json
POST /api/appoint/submit
{
    "userName": "李四",
    "phone": "13800138001",
    "deptName": "呼吸内科",
    "clinicName": "普通门诊",
    "doctorName": "王医生",
    "appointTime": "2024-01-15 09:00",
    "description": "咳嗽发热3天"
}
```

### 更新床位

```json
PUT /api/v1/bed/update/1
{
    "status": "已占用",
    "patientName": "赵六"
}
```

### 保存养生计划

```json
POST /api/health/plan/save
{
    "userId": 1,
    "age": 35,
    "ageGroup": "青年",
    "userInfo": "体质偏寒，易感冒",
    "planContent": "{\"diet\":\"温补饮食\",\"exercise\":\"太极拳\",\"sleep\":\"早睡早起\"}",
    "status": 1
}
```

### 查询用户养生计划

```
GET /api/health/plan/user/1
```

### 更新计划状态

```json
POST /api/health/plan/updateStatus
{
    "id": 1,
    "status": 0
}
```

### 删除养生计划

```
DELETE /api/health/plan/1
```

## 核心模块说明

### controller（控制器层）

- **UserController**：处理普通用户的注册、登录和信息查询。
- **DoctorController**：处理医生登录、密码修改，以及根据科室查询医生列表。登录返回字段包含 `sex`（性别）。
- **AppointmentController**：处理挂号相关业务，包括提交挂号、查询列表、更新状态。挂号号由系统自动生成（格式：`YY + 日期 + 6位随机数`）。
- **BedController**：处理床位管理，包括查询所有床位、更新床位状态和绑定病人。
- **HealthCarePlanController**：处理养生计划的增删改查，包括保存计划、查询全部/按用户/按ID查询、更新状态、删除计划。计划编号由系统自动生成（格式：`HCP + 时间戳 + 3位随机数`）。

### service（服务层）

- **SysUserService / SysUserServiceImpl**：系统用户服务接口与实现，封装用户相关的业务逻辑。
- 当前项目中部分 Controller 直接调用 Repository，属于简化设计，后续可将业务逻辑迁移至 Service 层。

### repository（数据访问层）

- **SysUserRepository**：系统用户数据访问，提供 `findByPhone` 查询方法。
- **DoctorRepository**：医生数据访问，提供 `findByPhone`、`findByDepartment` 自定义查询方法。
- **AppointmentRepository**：挂号数据访问，继承 `JpaRepository` 提供标准 CRUD。
- **BedRepository**：床位数据访问。
- **UserRepository**：基础用户数据访问。
- **HealthCarePlanRepository**：养生计划数据访问，提供 `findByPlanNo`、`findByUserIdOrderByCreateTimeDesc`、`findByAgeGroup`、`findByStatus`、`deleteByPlanNo` 等自定义查询方法。

### entity（实体层）

- **SysUser**：系统用户实体，包含姓名、手机号、身份证、性别及密码。
- **Doctor**：医生实体，包含姓名、性别、年龄、科室、身份证、手机号及密码。
- **Appointment**：挂号实体，包含挂号号、患者信息、科室医生、预约时间及状态。
- **Bed**：床位实体，包含楼层、房号、床位号、状态（空闲/已占用/维修中）及病人姓名。
- **User**：基础用户实体，包含用户名和密码。
- **HealthCarePlan**：养生计划实体，包含计划编号、用户ID、年龄、年龄段、用户信息、计划内容（JSON格式）、状态及创建时间。

## 项目结构

```
medical-ai-system/
├── pom.xml                              # Maven 项目配置
├── mvnw                                 # Maven Wrapper (Unix)
├── mvnw.cmd                             # Maven Wrapper (Windows)
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties     # Maven Wrapper 配置
├── src/
│   ├── main/
│   │   ├── java/com/example/medicalaisystem/
│   │   │   ├── MedicalAiSystemApplication.java   # Spring Boot 启动类
│   │   │   ├── CorsConfig.java                   # 跨域配置
│   │   │   ├── controller/
│   │   │   │   ├── AppointmentController.java    # 挂号接口
│   │   │   │   ├── BedController.java            # 床位管理接口
│   │   │   │   ├── DoctorController.java         # 医生接口
│   │   │   │   ├── HealthCarePlanController.java # 养生计划接口
│   │   │   │   └── UserController.java           # 用户接口
│   │   │   ├── Dto/
│   │   │   │   └── DoctorLoginRequest.java       # 医生登录请求 DTO
│   │   │   ├── entity/
│   │   │   │   ├── Appointment.java              # 挂号实体
│   │   │   │   ├── Bed.java                      # 床位实体
│   │   │   │   ├── Doctor.java                   # 医生实体
│   │   │   │   ├── HealthCarePlan.java           # 养生计划实体
│   │   │   │   ├── SysUser.java                  # 系统用户实体
│   │   │   │   └── User.java                     # 基础用户实体
│   │   │   ├── repository/
│   │   │   │   ├── AppointmentRepository.java    # 挂号数据访问
│   │   │   │   ├── BedRepository.java            # 床位数据访问
│   │   │   │   ├── DoctorRepository.java         # 医生数据访问
│   │   │   │   ├── HealthCarePlanRepository.java # 养生计划数据访问
│   │   │   │   ├── SysUserRepository.java        # 系统用户数据访问
│   │   │   │   └── UserRepository.java           # 基础用户数据访问
│   │   │   └── service/
│   │   │       ├── SysUserService.java           # 用户服务接口
│   │   │       └── impl/
│   │   │           └── SysUserServiceImpl.java   # 用户服务实现
│   │   └── resources/
│   │       └── application.yml                   # 应用配置文件
│   └── test/
│       └── java/com/example/medicalaisystem/
│           └── MedicalAiSystemApplicationTests.java  # 测试类
├── .gitignore
└── .gitattributes
```

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 4.0.6 | 应用框架 |
| Spring Data JPA | ORM 数据访问 |
| MySQL | 关系型数据库 |
| Lombok | 简化实体类代码 |
| Maven | 项目构建工具 |
