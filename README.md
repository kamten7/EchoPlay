# EchoPlay — 全栈视频分享平台

> 一个功能完备的视频分享平台，支持视频上传与转码、弹幕互动、用户社交、内容管理。前后端分离架构，前端 Vue 3 + TypeScript + Vite，后端 Spring Boot + MyBatis + Redis。

[![Java](https://img.shields.io/badge/Java-8-%23ED8B00?logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.18-%236DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.5-%234FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-6.0-%233178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-8.1-%23646CFF?logo=vite&logoColor=white)](https://vite.dev/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-%234479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-7-%23DC382D?logo=redis&logoColor=white)](https://redis.io/)
[![Docker](https://img.shields.io/badge/Docker-Compose-%232496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 目录

- [项目简介](#项目简介)
- [核心功能](#核心功能)
- [技术架构](#技术架构)
- [快速开始](#快速开始)
- [项目结构](#项目结构)
- [设计文档](#设计文档)
- [API 文档](#api-文档)

---

## 项目简介

EchoPlay 是一个前后端分离的全栈视频分享平台，灵感来源于 Bilibili，提供完整的视频上传、转码、播放、弹幕互动、用户社交以及后台管理能力。

**核心亮点**：
- 🎬 **视频处理管道**：分片上传 → FFmpeg 异步转码 → MP4 自适应播放
- 💬 **Canvas 弹幕引擎**：requestAnimationFrame 驱动的 60fps 弹幕渲染
- 🔐 **双端认证体系**：用户端 + 管理端独立 Token，Redis 会话管理
- 📊 **实时数据统计**：Redis ZSet 热搜排行、WebSocket 消息推送
- 🎨 **CSS 变量主题**：零运行时开销的浅色/深色双模式切换
- 🐳 **Docker 一键部署**：MySQL + Redis + MinIO + 双后端应用

---

## 核心功能

### 观众端
| 功能 | 技术要点 |
|------|----------|
| 视频浏览 | 分类导航、分页加载、推荐算法、热门榜单 |
| 视频搜索 | 关键词搜索、Redis ZSet 热搜排行 |
| 视频播放 | HTML5 播放器、进度拖拽、音量控制、全屏 |
| 弹幕系统 | Canvas 渲染、滚动/顶部/底部模式、自定义颜色 |
| 视频互动 | 点赞、收藏、投币（虚拟货币）、分享 |
| 评论系统 | 多级评论、置顶、用户自主删除 |
| 消息通知 | WebSocket 实时推送 |
| 用户主页 | 个人资料、播放历史、收藏夹 |

### 创作者端
| 功能 | 技术要点 |
|------|----------|
| 视频上传 | 分片上传 + 断点续传 |
| 稿件管理 | 状态筛选、模糊搜索、删除 |
| 数据统计 | 播放量、点赞数、粉丝增长 |

### 管理端
| 功能 | 技术要点 |
|------|----------|
| 仪表盘 | 实时数据统计 |
| 内容审核 | 视频审核（通过/拒绝）、推荐设置 |
| 用户管理 | 状态管理（正常/禁用） |
| 弹幕/评论管理 | 内容巡查与删除 |

---

## 技术架构

### 整体架构

```
┌────────────────────────────────────────────────────────────┐
│                       浏览器                                │
│  ┌──────────────────────┐  ┌───────────────────────────┐   │
│  │  EchoPlay Front       │  │  EchoPlay Admin (计划中)    │   │
│  │  (Vue 3, port 3000)   │  │  (Vue 3, port 3001)       │   │
│  └────────┬─────────────┘  └──────────┬────────────────┘   │
└───────────┼───────────────────────────┼────────────────────┘
            │  /api/*                    │  /api/*
            │                            │
┌───────────┼───────────────────────────┼────────────────────┐
│  ┌────────▼───────────┐  ┌───────────▼────────────────┐   │
│  │  my_video_platform-web         │  │  my_video_platform-admin       │   │
│  │  (Spring Boot, 7071)  │  │  (Spring Boot, 7070)            │   │
│  └──────────┬───────────┘  └──────────┬────────────────┘   │
│             └──────────┬──────────────┘                     │
│                        │                                    │
│           ┌────────────▼─────────────┐                      │
│           │   my_video_platform-common    │                  │
│           │   共享实体 / Mapper / Service │                  │
│           └────────────┬─────────────┘                      │
│                        │                                    │
│  ┌─────────────────────┼────────────────────┐               │
│  │          ┌──────────▼──────┐              │               │
│  │          │  MySQL 8.0      │              │               │
│  │          └─────────────────┘              │               │
│  │  ┌──────────────┐  ┌────────────────────┐ │               │
│  │  │  Redis 7      │  │  MinIO / OSS        │ │               │
│  │  │  会话/缓存/排行 │  │  对象存储            │ │               │
│  │  └──────────────┘  └────────────────────┘ │               │
│  │                  ┌────────────────────┐   │               │
│  │                  │  FFmpeg (异步转码)   │   │               │
│  │                  └────────────────────┘   │               │
│  └──────────────────────────────────────────┘                │
└─────────────────────────────────────────────────────────────┘
```

### 后端技术选型

| 技术 | 用途 | 选型理由 |
|------|------|----------|
| **Spring Boot 2.7** | 应用框架 | 企业级成熟度，生态丰富 |
| **MyBatis** | ORM | 相比 JPA 更灵活的 SQL 控制，适合复杂查询 |
| **MySQL 8.0** | 数据库 | 关系型数据 ACID 保证 |
| **Redis 7** | 缓存/会话 | Token 管理、热搜 ZSet、在线人数计数 |
| **Redisson** | 分布式锁 | 并发安全的资源访问控制 |
| **FFmpeg** | 视频转码 | 工业级音视频处理，自动编码器检测降级 |
| **MinIO** | 本地对象存储 | S3 兼容，可无缝切换阿里云 OSS |
| **WebSocket** | 实时推送 | 弹幕同步 + 消息通知 |
| **EasyCaptcha** | 验证码 | 算术验证码防机器注册 |

### 前端技术选型

| 技术 | 用途 | 选型理由 |
|------|------|----------|
| **Vue 3** | UI 框架 | Composition API + `<script setup>` 提升代码复用性 |
| **TypeScript** | 类型系统 | 编译时错误检测，减少线上 Bug |
| **Vite** | 构建工具 | ESM 原生 HMR，冷启动 < 500ms |
| **Pinia** | 状态管理 | Vue 3 官方推荐，模块化 + 完整 TS 推导 |
| **Vue Router** | 路由 | 懒加载、导航守卫、滚动行为控制 |
| **Frontloom** | 工程规范 | 语义化 HTML / 无障碍 / 安全 / 响应式基线 |

---

## 快速开始

### 前置依赖

| 工具 | 版本 | 用途 |
|------|------|------|
| JDK | 1.8+ | 后端编译运行 |
| Maven | 3.6+ | 后端构建 |
| Node.js | 18+ | 前端构建 |
| Docker Desktop | 最新 | 容器化中间件（推荐） |

### 1. 克隆项目

```bash
git clone https://github.com/kamten07/EchoPlay.git
cd EchoPlay
```

### 2. 启动中间件（Docker）

```bash
# 复制环境变量
cp .env.example .env
# 编辑 .env 中的数据库密码和存储路径

# 启动 MySQL + Redis + MinIO
docker compose -f docker-compose.dev.yml up -d
```

### 3. 启动后端

```bash
# 编译
mvn clean package -DskipTests

# 用户端 API（端口 7071）
java -jar my_video_platform-web/target/my_video_platform-web-1.0.jar

# 管理端 API（端口 7070，新终端）
java -jar my_video_platform-admin/target/my_video_platform-admin-1.0.jar
```

### 4. 启动前端

```bash
cd My_video_platform_front
npm install
npm run dev    # http://localhost:3000
```

### 5. 访问

| 服务 | 地址 |
|------|------|
| 前端首页 | http://localhost:3000 |
| 用户端 API | http://localhost:7071 |
| 管理端 API | http://localhost:7070 |
| MinIO 控制台 | http://localhost:9001 |

---

## 项目结构

```
EchoPlay/
├── My_video_platform_front/          # 前端（Vue 3 + Vite + TypeScript）
│   ├── src/
│   │   ├── types/                    # TypeScript 类型
│   │   ├── router/                   # Vue Router
│   │   ├── stores/                   # Pinia（video, theme）
│   │   ├── services/                 # API 服务层
│   │   ├── mock/                     # Mock 数据
│   │   ├── components/               # 组件（layout/, home/, common/）
│   │   ├── views/                    # 页面
│   │   └── assets/styles/            # Design Tokens + Global CSS
│   ├── API_DOCS.md                   # 80 个 API 接口文档
│   └── DESIGN_ARCHITECTURE.md        # 架构设计文档
│
├── my_video_platform-web/            # 用户端 API（Spring Boot, port 7071）
│   └── src/main/java/com/myvideoplatform/web/controller/
│
├── my_video_platform-admin/          # 管理端 API（Spring Boot, port 7070）
│   └── src/main/java/com/myvideoplatform/admin/controller/
│
├── my_video_platform-common/         # 共享模块
│   ├── entity/                       # PO/DTO/VO/枚举
│   ├── mappers/                      # MyBatis Mapper
│   ├── service/                      # 业务逻辑
│   └── utils/                        # 工具类（FFmpeg, Redis, String）
│
├── docker/                           # Docker 配置
│   └── mysql/init.sql                # 数据库建表
├── docker-compose.yml                # 生产部署编排
├── docker-compose.dev.yml            # 开发环境编排
├── .env.example                      # 环境变量模板
├── pom.xml                           # Maven 父 POM
└── SETUP.md                          # 详细配置指南
```

---

## 设计文档

详细架构设计文档：[DESIGN_ARCHITECTURE.md](./My_video_platform_front/DESIGN_ARCHITECTURE.md)

涵盖：
- 系统架构分层设计
- 关键数据流（视频上传、弹幕渲染、用户互动）
- 技术决策记录（ADR）
- 安全设计
- 性能优化策略
- 设计系统规范

---

## API 文档

完整 API 接口文档：[API_DOCS.md](./My_video_platform_front/API_DOCS.md)

- **80 个 API 接口**，14 个业务模块
- 前后端实现状态标注
- 请求/响应格式 + 数据模型

---

## 许可证

本项目基于 MIT 许可证开源。
