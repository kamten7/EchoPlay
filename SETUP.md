# My Video Platform 项目配置与运行指南
## 项目概述

My Video Platform 是一个全栈视频分享平台后端，灵感来源于 Bilibili。项目采用 Spring Boot + MyBatis + MySQL + Redis 技术栈，支持视频分片上传、FFmpeg 异步转码、弹幕系统、用户社交等完整功能。
**GitHub 仓库**: https://github.com/kamten7/My_video_platform.git

**前端仓库**: 前端代码（Vue 3 + TypeScript + Vite）在独立仓库 My_video_platform_front 中。
---

## 技术栈

### 后端技术
| 技术 | 版本 | 用途 |
|------|------|------|
| **Java** | 1.8+ | 运行环境 |
| **Spring Boot** | 2.7.18 | 应用框架 |
| **MyBatis** | 1.3.2 | ORM 框架 |
| **MySQL** | 8.0+ | 关系数据库 |
| **Redis** | 7.x+ | 缓存/会话/排行 |
| **Redisson** | 3.17.7 | 分布式锁 |
| **FFmpeg** | 4.x+ | 视频转码 |
| **MinIO** | latest | 本地对象存储 |
| **阿里云 OSS** | 3.17.4 | 云端对象存储（可选） |
| **WebSocket** | - | 实时消息推送 |
| **EasyCaptcha** | 1.6.2 | 算术验证码 |
| **FastJSON** | 1.2.83 | JSON 序列化 |
| **Lombok** | 1.18.34 | 代码简化 |
| **Docker** | - | 容器化部署 |
| **Maven** | 3.6+ | 项目构建 |

### 前端技术（独立仓库）
| 技术 | 用途 |
|------|------|
| **Vue 3 + Composition API** | UI 框架 |
| **TypeScript** | 类型系统 |
| **Vite** | 构建工具 |
| **Element Plus** | UI 组件库 |
| **Pinia** | 状态管理 |
| **ECharts** | 数据可视化（管理端） |
| **Canvas API** | 弹幕渲染 |

---

## 环境要求

### 使用 Docker 部署（推荐）

| 工具 | 最低版本 | 说明 |
|------|----------|------|
| Docker Desktop | 最新 | Windows 版，图形化管理界面 |
| Docker Compose | 内置 | Docker Desktop 自动包含 |
| JDK 1.8 | - | 仅本地开发时需要 |
| Maven 3.6+ | - | 仅本地编译时需要 |

### 本地开发（不使用 Docker）
| 工具 | 最低版本 | 检查命令 |
|------|----------|----------|
| JDK | 1.8 | `java -version` |
| Maven | 3.6 | `mvn -version` |
| MySQL | 8.0 | `mysql --version` |
| Redis | 6.0 | `redis-cli --version` |
| FFmpeg | 4.x | `ffmpeg -version` |

---

## 快速开始 - Docker 部署（推荐）

### 第 1 步：克隆项目

```bash
git clone https://github.com/kamten7/My_video_platform.git
cd My_video_platform
```

### 第 2 步：配置环境变量

```powershell
# 复制模板文件
copy .env.example .env

# 用记事本或 VS Code 打开 .env，修改以下关键配置：
#   MYSQL_ROOT_PASSWORD    - MySQL root 密码
#   MYSQL_PASSWORD         - MySQL 应用用户密码
#   REDIS_PASSWORD         - Redis 密码
#   MINIO_ROOT_PASSWORD    - MinIO 密码
#   WINDOWS_PROJECT_DIR    - 文件存储目录（Windows 路径，如 D:/webser/my_video_platform）
#   WINDOWS_FFMPEG_PATH    - FFmpeg 路径（可选，无则跳过转码）
```

### 第 3 步：创建文件存储目录

```powershell
# 在 PowerShell 中执行
mkdir D:\webser\my_video_platform\video\upload -Force
mkdir D:\webser\my_video_platform\video\original -Force
mkdir logs -Force
```

> 确保 `.env` 中的 `WINDOWS_PROJECT_DIR` 与此目录一致。
### 第 4 步：编译后端

```powershell
# 在项目根目录执行
mvn clean package -DskipTests
```

编译成功后会生成：
- `my_video_platform-web/target/my_video_platform-web-1.0.jar`
- `my_video_platform-admin/target/my_video_platform-admin-1.0.jar`

### 第 5 步：启动 Docker 服务

```powershell
# 一键启动所有中间件 + 后端应用
docker-compose up -d

# 查看运行状态
docker-compose ps

# 查看日志
docker-compose logs -f mysql      # MySQL 日志
docker-compose logs -f redis      # Redis 日志
docker-compose logs -f minio      # MinIO 日志
docker-compose logs -f my_video_platform-web    # 用户端 API 日志
docker-compose logs -f my_video_platform-admin  # 管理端 API 日志
```

### 第 6 步：验证服务

```powershell
# 等待 10-20 秒让 MySQL 完全启动后，测试接口
curl http://localhost:7071/account/checkCode
curl http://localhost:7070/account/login -d "account=admin&password=admin123"
```

### 第 7 步：访问

| 服务 | 地址 | 说明 |
|------|------|------|
| 用户端 API | http://localhost:7071 | 后端 REST 接口 |
| 管理端 API | http://localhost:7070 | 管理端 REST 接口 |
| MinIO 控制台 | http://localhost:9001 | 文件管理（minioadmin / 密码） |
| MinIO API | http://localhost:9000 | 对象存储接口 |

### 常用 Docker 命令

```powershell
# 停止所有服务
docker-compose down

# 停止并删除数据卷（⚠️ 会清空所有数据！）
docker-compose down -v

# 重启某个服务
docker-compose restart my_video_platform-web

# 进入 MySQL 容器
docker exec -it mvp-mysql mysql -uroot -p

# 进入 Redis 容器
docker exec -it mvp-redis redis-cli -a <REDIS_PASSWORD>

# 更新配置后重新构建
docker-compose up -d --build
```

---

## 快速开始 - 本地开发
### 第 1 步：配置数据库
1. 启动 MySQL 服务
2. 创建数据库：

```sql
CREATE DATABASE my_video_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

3. 执行建表脚本：`docker/mysql/init.sql`

### 第 2 步：配置 Redis

```powershell
# 确保 Redis 服务已启动（默认端口 6379）
redis-cli ping   # 应返回 PONG
```

### 第 3 步：修改配置文件

编辑以下配置文件：
**`my_video_platform-web/src/main/resources/application.yml`**：
```yaml
spring:
  datasource:
    password: 你的数据库密码
  redis:
    password: 你的Redis密码

project:
  folder: d:/webser/my_video_platform/   # 文件存储目录

ffmpeg:
  path: C:/Program Files/ffmpeg/bin/ffmpeg.exe  # 可选
```

**`my_video_platform-admin/src/main/resources/application.yml`**：
```yaml
spring:
  datasource:
    password: 你的数据库密码
  redis:
    password: 你的Redis密码

admin:
  password: admin123   # 建议修改默认密码
```

### 第 4 步：编译并启动
```powershell
# 编译
mvn clean package -DskipTests

# 终端 1：用户端 API（端口 7071）
java -jar my_video_platform-web/target/my_video_platform-web-1.0.jar

# 终端 2：管理端 API（端口 7070）
java -jar my_video_platform-admin/target/my_video_platform-admin-1.0.jar
```

---

## 阿里云 OSS 配置（可选）

当你需要使用阿里云 OSS 替代 MinIO 时：

1. 打开 `.env` 文件
2. 取消以下配置的注释并填入你的密钥：
```
OSS_ENDPOINT=oss-cn-shenzhen.aliyuncs.com
OSS_ACCESS_KEY_ID=LTAI5t...
OSS_ACCESS_KEY_SECRET=xxxxxx
OSS_BUCKET_NAME=your-bucket
```
3. 重启服务：
```powershell
docker-compose restart my_video_platform-web my_video_platform-admin
```

> **安全提示**：OSS 密钥只存在于 `.env` 文件中，该文件已被 `.gitignore` 排除，不会提交到 Git。
---

## 项目模块结构

```
my_video_platform/
├── docker-compose.yml          # Docker 编排文件
├── .env.example                # 环境变量模板
├── .env                        # 你的私有配置（不提交到 Git）
├── docker/
│   └── mysql/
│       └── init.sql            # 数据库初始化脚本
├── my_video_platform-web/               # 用户端 API（端口 7071）
│   ├── Dockerfile
│   └── src/main/resources/
│       ├── application.yml
│       └── logback-spring.xml
├── my_video_platform-admin/             # 管理端 API（端口 7070）
│   ├── Dockerfile
│   └── src/main/resources/
│       ├── application.yml
│       └── logback-spring.xml
└── my_video_platform-common/            # 共享模块
    ├── entity/                 # 数据实体
    ├── mappers/                # MyBatis Mapper
    ├── service/                # 业务逻辑
    └── utils/                  # 工具类
```

---

## 常见问题

### 1. Docker 启动后 MySQL 连不上
- 等待 15-30 秒让 MySQL 完全初始化
- 查看日志：`docker-compose logs mysql`
- 确认 `.env` 中的密码与 `init.sql` 一致

### 2. MinIO 控制台打不开
- 确认端口 9001 未被占用
- 访问 http://localhost:9001，使用 `.env` 中的 MINIO_ROOT_USER/PASSWORD

### 3. 视频上传/转码失败
- 确认 `.env` 中 `WINDOWS_FFMPEG_PATH` 指向正确的 FFmpeg 可执行文件
- 如果不安装 FFmpeg，转码功能会自动跳过

### 4. 端口被占用
```powershell
# 查看端口占用
netstat -ano | findstr "3306"
netstat -ano | findstr "6379"
netstat -ano | findstr "9000"

# 修改 docker-compose.yml 中的端口映射，例如："3307:3306"
```

### 5. 文件存储目录找不到
- 确保 `.env` 中设置的 `WINDOWS_PROJECT_DIR` 目录存在
- Docker Desktop 需要在 Settings → Resources → File Sharing 中添加该盘符

### 6. Docker Desktop 文件共享问题
如果在 Windows 上遇到挂载目录权限问题：
1. 打开 Docker Desktop → Settings
2. 进入 Resources → File Sharing
3. 添加你的存储盘符（如 `D:`）
4. 点击 Apply & Restart

---

## 开发建议
- **IDE**：推荐使用 IntelliJ IDEA，直接导入 Maven 项目
- **数据库管理**：推荐 Navicat 或 DBeaver（连接 localhost:3306）
- **Redis 管理**：推荐 Redis Insight 或 Another Redis Desktop Manager
- **MinIO 管理**：浏览器访问 http://localhost:9001
- **API 测试**：可使用 Postman 或 Apifox

---

## 许可证
本项目基于 MIT 许可证开源。