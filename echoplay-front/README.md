# EchoPlay 前端

> 仿 Bilibili 风格的视频分享平台前端，基于 Vue 3 + TypeScript + Vite 构建。

[![Vue](https://img.shields.io/badge/Vue-3.5-%234FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-6.0-%233178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-8.1-%23646CFF?logo=vite&logoColor=white)](https://vite.dev/)
[![Pinia](https://img.shields.io/badge/Pinia-3.0-%23FFD859?logo=vue.js&logoColor=white)](https://pinia.vuejs.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](../LICENSE)

---

## 目录

- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [设计系统](#设计系统)
- [开发指南](#开发指南)
- [架构设计](#架构设计)
- [API 对接](#api-对接)

---

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | ^3.5 | 渐进式 UI 框架 — Composition API + `<script setup>` |
| TypeScript | ~6.0 | 静态类型检查，减少运行时错误 |
| Vite | ^8.1 | 极速 HMR 构建工具，ESM 原生支持 |
| Pinia | ^3.0 | Vue 3 官方状态管理，TypeScript 友好 |
| Vue Router | ^4.6 | SPA 路由，懒加载 + 导航守卫 |
| Frontloom | ^0.1 | 前端工程规范（语义化 HTML / 无障碍 / 安全） |

### 为什么选择这些技术

- **Vite 而非 Webpack**：原生 ESM 开发服务器，冷启动 < 500ms，HMR < 100ms
- **Pinia 而非 Vuex**：官方推荐的下一代 Store，模块化设计无需 `modules` 嵌套，完整的 TS 推导
- **纯 CSS 自定义属性**而非 Tailwind：利用 CSS `@layer` + `data-theme` 实现零运行时主题切换，构建产物更小

---

## 项目结构

```
src/
├── main.ts                    # 应用入口（挂载 Pinia + Router）
├── App.vue                    # 根组件
│
├── types/index.ts             # 全局类型定义（Video, User, Comment, API 响应...）
├── router/index.ts            # 路由配置（懒加载 + 标题守卫）
│
├── services/                  # API 服务层
│   ├── core.ts                # 基础 HTTP 客户端（GET/POST + form-urlencoded）
│   ├── video.ts               # 视频相关 API
│   ├── category.ts            # 分类 API
│   └── index.ts
│
├── stores/                    # Pinia 状态管理
│   ├── video.ts               # 视频状态（推荐/热门/最新）
│   └── theme.ts               # 主题状态（浅色/深色切换 + system 跟随）
│
├── mock/index.ts              # Mock 数据（24 条视频 + 4 张 Banner）
│
├── composables/               # Vue Composables（可复用逻辑）
│
├── components/
│   ├── layout/
│   │   ├── AppLayout.vue      # 全局布局（Header + Sidebar + Main）
│   │   ├── AppHeader.vue      # 顶部导航（Logo + 导航标签 + 搜索 + 主题切换）
│   │   └── AppSidebar.vue     # 侧边栏（分类 + 热搜 + 页脚）
│   ├── home/
│   │   ├── BannerCarousel.vue # 轮播图（自动播放 + 箭头 + 圆点）
│   │   ├── VideoCard.vue      # 视频卡片（封面 + 时长 + 悬浮播放图标）
│   │   └── VideoGrid.vue      # 视频网格（标题 + 响应式布局）
│   └── common/                # 通用组件（预留）
│
├── views/
│   └── HomeView.vue           # 首页（Banner + 编辑推荐 + 热门 + 最新）
│
└── assets/styles/
    ├── tokens.css             # 设计 Token（140+ 个 CSS 自定义属性，双主题）
    └── global.css             # 全局 Reset + 基础样式
```

---

## 设计系统

### 色彩

| Token | 浅色模式 | 深色模式 | 用途 |
|-------|---------|---------|------|
| `--color-primary` | `#1A6DF5` | `#4D94FF` | 品牌主色 |
| `--color-bg-page` | `#F4F5F7` | `#0D1117` | 页面背景 |
| `--color-bg-container` | `#FFFFFF` | `#161B22` | 容器/Card 背景 |
| `--color-text-primary` | `#18191C` | `#E8EAED` | 主文字 |
| `--color-border` | `#E3E5E7` | `#30363D` | 边框 |

### 主题切换

- `stores/theme.ts` — Pinia Store 驱动
- `data-theme` 属性挂载在 `<html>` 上，CSS 变量级联自动切换
- 支持三种模式：浅色 / 深色 / 跟随系统
- 用户手动选择持久化到 `localStorage`
- 监听 `prefers-color-scheme` 媒体查询自动响应系统变化

### 排版

- 字体栈：`PingFang SC` → `HarmonyOS Sans SC` → `MiSans` → `Noto Sans SC` → system-ui
- 等比字号：12 / 13 / 14 / 16 / 18 / 20 / 24 / 32 px
- 行高：1.25 / 1.5 / 1.75

---

## 开发指南

```bash
# 安装依赖
npm install

# 启动开发服务器（http://localhost:3000）
npm run dev

# 类型检查 + 构建
npm run build

# 预览构建产物
npm run preview
```

### 代码规范

本项目遵循 [Frontloom](https://github.com/frontloom/frontloom) 前端工程规范：

1. **`<script setup lang="ts">`** — 所有新组件使用 Composition API
2. **Props 类型声明** — `defineProps<T>()` + `withDefaults`
3. **CSS 变量** — 禁止硬编码颜色/间距，统一使用 design token
4. **语义化 HTML** — `<button>` 而非 `<div onClick>`，`<nav>` 用于导航
5. **无障碍优先** — `aria-label`、`role`、键盘焦点、`sr-only` 文本
6. **完整 UI 状态** — loading / empty / error / success 均需处理

---

## 架构设计

### 数据流

```
用户操作 → 组件（emit / v-model）
    ↓
Pinia Store（状态管理）
    ↓
Service 层（API 封装）→ Mock 降级（无后端时）
    ↓
HTTP 请求（form-urlencoded + Cookie 认证）
    ↓
后端 API → MySQL / Redis / MinIO
```

### 设计决策

1. **Mock 优先开发**：`mock/index.ts` 提供完整 mock 数据，前端可独立开发调试，无需后端运行
2. **Service 抽象层**：`services/` 封装所有 API 调用，组件不直接 `fetch`，便于统一错误处理、请求拦截、Mock 切换
3. **Store 按领域拆分**：`video`（内容）、`theme`（主题），各 Store 独立不耦合
4. **CSS 变量主题**：零运行时开销，浏览器原生 CSS 变量级联，`data-theme` 切换瞬时生效
5. **响应式断点**：4 列 → 3 列 → 2 列 → 1 列，移动端侧边栏隐藏

---

## API 对接

完整 API 文档见 [API_DOCS.md](./API_DOCS.md)。

当前已对接的 API：
- `GET /video/loadRecommendVideo` — 推荐视频
- `GET /video/loadVideo` — 分页视频列表
- `GET /video/loadHotVideoList` — 热门视频
- `GET /video/getVideoRecommend` — 侧栏推荐
- `GET /category/loadAllCategory` — 分类树

待前端实现页面（按优先级）：视频详情页 → 登录/注册 → 搜索结果 → 用户主页 → 创作中心 → 管理后台
