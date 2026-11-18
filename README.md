# 企业通讯录管理系统

一个基于 **Spring Boot + Vue 3 + MySQL** 的前后端分离企业通讯录系统，支持部门树形管理、员工与客户信息维护、基于角色的权限控制（超级管理员 / 管理员 / 员工）以及操作日志审计。

## 技术栈

| 层 | 技术 |
|---|---|
| 后端 | Spring Boot 3、Spring Security + JWT、MyBatis-Plus、Druid、MySQL 8 |
| 前端 | Vue 3（组合式 API）、TypeScript、Vite、Element Plus、Pinia、Vue Router |
| 文档 | springdoc-openapi / Knife4j |

## 功能模块

- **部门管理**：部门的新增、修改、删除，树形结构展示组织架构
- **员工管理**：工号、姓名、性别、部门、角色、联系方式等字段的增删改查
- **客户管理**：外部联系人维护，支持"私有 / 共享"隐私级别控制可见性
- **操作日志**：记录关键操作的数据变更快照（old_data / new_data）与操作时间，便于审计
- **登录认证**：JWT 无状态认证，接口按角色鉴权，路由守卫按角色跳转
- **个人信息**：查看与维护当前登录人的资料

## 目录结构

```
CorporateDirectory
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/com/wei/
│   │   ├── controller/      # REST 接口
│   │   ├── service/         # 业务逻辑
│   │   ├── entity/          # 实体
│   │   ├── config/          # Security / JWT / MyBatis-Plus 配置
│   │   └── response/        # 统一返回结构 R
│   └── src/main/resources/application.yml
├── frontend/                # Vue 3 前端
│   └── src/{api,views,components,router,stores}
└── init.sql                 # 建库建表 + 演示数据
```

## 快速开始

### 1. 准备数据库

```sql
-- 用 MySQL 客户端执行 init.sql（会创建库表并写入演示数据）
source init.sql;
```

默认库名：`corporate_directory_wei`

### 2. 启动后端

```bash
cd backend
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

- 服务端口：`8082`，上下文路径：`/wei`
- 接口文档：http://localhost:8082/wei/swagger-ui.html

数据库账号密码默认 `root / 123456`，可用环境变量覆盖：

```bash
DB_USERNAME=root
DB_PASSWORD=你的密码
JWT_SECRET=自定义密钥（至少64字符）
```

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

打开 http://localhost:5173

### 4. 演示账号

| 角色 | 工号 | 密码 |
|---|---|---|
| 超级管理员 | SUPER001 | 123456 |
| 管理员 | ADMIN001 | 123456 |
| 员工 | EMP001 | 123456 |

## 设计要点

- **统一响应体**：所有接口返回 `R<T>`（code / message / data），异常由 `ControllerExceptionHandler` 统一处理
- **认证与鉴权**：`JwtRequestFilter` 解析 Bearer Token，`SecurityConfig` 放行登录与文档接口，其余请求需认证
- **权限模型**：员工表 `emp_role` 区分三种角色，前端路由守卫 + 后端接口双重校验
- **审计日志**：写操作记录操作人、目标表、变更前后快照，供日志查询模块使用
