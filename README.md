# 水果批发仓库管理系统

一个功能完整的水果批发仓库管理系统，支持水果管理、库存管理、入库出库、供应商和客户管理等功能。

## 🛠 技术栈

- **Frontend**: Vue2 + VueRouter + Element UI + Axios
- **Backend**: Spring Boot 3 + Spring Data JPA + MySQL
- **Database**: MySQL 8.0 (UTF-8 编码)
- **Containerization**: Docker + Docker Compose

## 🚀 启动指南 (How to Run)

1. 确保 Docker Desktop 已启动
2. 在项目根目录执行：

```bash
docker compose up --build
```

3. 等待容器启动完成（首次启动需要下载依赖，约 3-5 分钟）
4. 看到以下日志表示启动成功：
   - `fruit-warehouse-backend` 输出 `Started FruitWarehouseApplication`
   - `fruit-warehouse-frontend` 输出 `start worker process`

## 🔗 服务地址 (Services)

| 服务 | 地址 | 说明 |
|------|------|------|
| Frontend | http://localhost:3000 | 前端界面 |
| Backend API | http://localhost:8080/api | 后端接口 |
| Database | localhost:3306 | MySQL 数据库 |

## 🧪 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 操作员 | operator1 | 123456 |

## 📋 功能模块

### 首页概览
- 水果种类统计、今日入库/出库统计
- 库存预警提醒
- 分类统计图表
- 快捷操作入口

### 水果管理
- 水果信息的增删改查
- 按分类、状态筛选
- 库存预警标识

### 分类管理
- 水果分类的增删改查
- 支持自定义图标和排序

### 入库管理
- 新增入库记录
- 自动更新库存
- 入库记录查询

### 出库管理
- 新增出库记录
- 库存校验（防止超卖）
- 出库记录查询

### 供应商管理
- 供应商信息维护
- 供应商状态管理

### 客户管理
- 客户信息维护
- VIP 客户标识

## 🗄 数据库设计

| 表名 | 说明 |
|------|------|
| users | 用户表 |
| categories | 水果分类表 |
| fruits | 水果表 |
| suppliers | 供应商表 |
| customers | 客户表 |
| stock_in_records | 入库记录表 |
| stock_out_records | 出库记录表 |

## 📁 项目结构

```
taskId587/
├── backend/                    # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/fruit/warehouse/
│   │       ├── controller/     # 控制器层
│   │       ├── service/        # 服务层
│   │       ├── repository/     # 数据访问层
│   │       ├── entity/         # 实体类
│   │       ├── dto/            # 数据传输对象
│   │       └── config/         # 配置类
│   ├── src/main/resources/
│   │   └── application.yml     # 应用配置
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                   # Vue2 前端
│   ├── src/
│   │   ├── views/              # 页面组件
│   │   ├── router/             # 路由配置
│   │   ├── api/                # API 接口
│   │   └── assets/             # 静态资源
│   ├── public/
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
├── mysql/
│   └── init.sql                # 数据库初始化脚本
├── docker-compose.yml          # Docker 编排配置
├── .gitignore
├── .dockerignore
└── README.md
```

## 🔧 开发说明

### 本地开发

**后端开发：**
```bash
cd backend
mvn spring-boot:run
```

**前端开发：**
```bash
cd frontend
npm install
npm run serve
```

### API 接口

| 模块 | 接口前缀 | 说明 |
|------|----------|------|
| 认证 | `/api/auth` | 登录 |
| 仪表盘 | `/api/dashboard` | 统计数据 |
| 水果 | `/api/fruits` | 水果 CRUD |
| 分类 | `/api/categories` | 分类 CRUD |
| 供应商 | `/api/suppliers` | 供应商 CRUD |
| 客户 | `/api/customers` | 客户 CRUD |
| 库存 | `/api/stock` | 入库/出库 |

## 🐳 Docker 命令

```bash
# 启动服务
docker compose up -d

# 查看日志
docker compose logs -f

# 停止服务
docker compose down

# 重新构建
docker compose up --build -d

# 清理数据卷
docker compose down -v
```

## 📝 注意事项

1. 首次启动时，MySQL 需要初始化数据库，后端可能需要等待
2. 数据库使用 UTF-8 编码，支持中文存储
3. 前端通过 Nginx 代理访问后端 API
4. 数据持久化在 Docker Volume 中
