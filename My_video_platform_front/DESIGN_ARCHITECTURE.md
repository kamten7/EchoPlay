# EchoPlay — 设计与架构文档

> 本文档记录 EchoPlay 前端工程的设计决策、架构模式、数据流和工程规范。  
> 目标受众：代码审查者、技术面试官、新加入团队的开发者。

---

## 目录

1. [架构概览](#1-架构概览)
2. [分层设计](#2-分层设计)
3. [关键数据流](#3-关键数据流)
4. [组件设计原则](#4-组件设计原则)
5. [状态管理策略](#5-状态管理策略)
6. [主题系统设计](#6-主题系统设计)
7. [路由设计](#7-路由设计)
8. [性能优化](#8-性能优化)
9. [安全设计](#9-安全设计)
10. [无障碍设计](#10-无障碍设计)
11. [响应式策略](#11-响应式策略)
12. [工程规范](#12-工程规范)
13. [技术决策记录(ADR)](#13-技术决策记录adr)
14. [待改进项](#14-待改进项)

---

## 1. 架构概览

```
┌─────────────────────────────────────────────────────────────┐
│                     View Layer (views/)                      │
│   HomeView, VideoDetailView (planned), SearchView (planned)  │
├─────────────────────────────────────────────────────────────┤
│                  Component Layer (components/)               │
│  ┌──────────┐  ┌──────────┐  ┌────────────┐                │
│  │  layout/  │  │  home/   │  │  common/   │                │
│  │ AppLayout │  │ Banner   │  │ (planned)  │                │
│  │ AppHeader │  │ VideoCard│  │            │                │
│  │ AppSidebar│  │ VideoGrid│  │            │                │
│  └──────────┘  └──────────┘  └────────────┘                │
├─────────────────────────────────────────────────────────────┤
│                   Store Layer (stores/)                      │
│  ┌───────────┐  ┌───────────┐                               │
│  │  video.ts  │  │  theme.ts │ (Pinia — Composition API)     │
│  └───────────┘  └───────────┘                               │
├─────────────────────────────────────────────────────────────┤
│                  Service Layer (services/)                   │
│  ┌───────────┐  ┌───────────┐  ┌────────────┐              │
│  │  core.ts   │  │ video.ts  │  │ category.ts│              │
│  │ HTTP Client│  │ Video API │  │ Category   │              │
│  └───────────┘  └───────────┘  └────────────┘              │
├─────────────────────────────────────────────────────────────┤
│  Mock Layer (mock/)      │    API Server (Spring Boot)       │
│  开发降级 / 独立调试       │    form-urlencoded + Cookie Auth  │
└─────────────────────────────────────────────────────────────┘
```

### 架构原则

1. **单向数据流**：View → (emit) → Store → (reactive) → View
2. **关注点分离**：API 调用、状态管理、UI 渲染各自独立
3. **优先组合**：Slot 组合优于 Props 配置
4. **渐进增强**：HTML 语义层正确 → CSS 层响应式 → JS 层增强

---

## 2. 分层设计

### 2.1 View Layer（页面层）

`views/` 目录下的每个文件对应一个路由页面。页面负责：
- 组合 Layout 和业务组件
- 将 Store 数据通过 `storeToRefs()` 传递给子组件
- 处理路由级事件（如搜索跳转）

**设计约束**：页面不直接调用 `fetch`，不包含复杂业务逻辑。所有数据通过 Store 获取。

```typescript
// HomeView.vue — page concerns only
const store = useVideoStore();
const { banners, recommendVideos } = storeToRefs(store);
// Data flows down to children via props
```

### 2.2 Component Layer（组件层）

组件分为三类：

| 目录 | 职责 | 复用范围 |
|------|------|---------|
| `layout/` | 全局布局骨架（Header, Sidebar, Layout） | 全站 |
| `home/` | 首页业务组件（Banner, VideoCard, VideoGrid） | 按需 |
| `common/` | 通用 UI 原语（按钮、输入框、Loading 等） | 全站 |

**组件 API 设计规范**：
- Props 使用 `defineProps<T>()` 类型声明
- 事件使用 `defineEmits<T>()` 类型声明
- 双向绑定使用 `defineModel<T>()`（Vue 3.4+）
- 组合优于配置 — 优先用 Slot 而非大量 Boolean Props

```typescript
// Good — composable via slots
<VideoGrid title="热门" :videos="list" more-link="/hot" />

// Good — VideoGrid internally composes VideoCard via iteration
// VideoCard is a presentational component, no store dependency
```

### 2.3 Store Layer（状态层）

每个 Pinia Store 使用 Composition API 风格（`defineStore` + `setup` 函数）：

```typescript
export const useVideoStore = defineStore("video", () => {
  const videos = ref<VideoItem[]>([]);
  const loading = ref(false);

  // Derived state via computed — cached, lazy-evaluated
  const hotVideos = computed(() =>
    [...videos.value].sort((a, b) => b.playCount - a.playCount)
  );

  // Actions are plain async functions
  async function fetchVideos() { ... }

  return { videos, loading, hotVideos, fetchVideos };
});
```

**Store 设计原则**：
- 每个 Store 对应一个领域（video, theme, user, comment）
- Store 之间不直接引用，通过组件或 composable 协调
- 派生状态用 `computed`，不在组件中重复计算

### 2.4 Service Layer（服务层）

`services/core.ts` 封装了统一的 HTTP 客户端：

```typescript
class ApiClient {
  // Supports GET with query params, POST with form-urlencoded body
  // Automatically sets credentials: "include" for Cookie auth
  // Content-Type: application/x-www-form-urlencoded (matches backend)
}
```

**设计决策**：为什么用 form-urlencoded 而非 JSON？

后端 Spring Boot Controller 默认从 `@RequestParam` 接收参数，使用 `application/x-www-form-urlencoded` 无需修改后端。后续可通过 `Content-Type` 协商逐步迁移到 JSON。

### 2.5 Mock Layer（降阶层）

`mock/index.ts` 提供 24 条视频 + 4 张 Banner 的完整 mock 数据。

**降级策略**：
```
Store 初始化 → 尝试 API 调用
    ↓ 成功 → 用真实数据替换
    ↓ 失败 → 保留 mock 默认值（离线可开发）
```

这使得前端可以完全不依赖后端独立开发调试。后端 API 就绪后，只需配置 Vite proxy 即可无缝切换。

---

## 3. 关键数据流

### 3.1 首页加载流程

```
App.vue (RouterView)
  └─→ HomeView.vue
        ├─→ useVideoStore() → mock 初始化 videos[], banners[]
        ├─→ BannerCarousel (:banners)
        ├─→ VideoGrid (:videos) → VideoCard (:video) × N
        └─→ 用户点击导航 → emit("search", keyword)
```

### 3.2 主题切换流程

```
用户点击 Header 太阳/月亮按钮
  └─→ themeStore.toggle()
        ├─→ mode.value = "dark" | "light"
        ├─→ watchEffect → document.documentElement.setAttribute("data-theme", mode)
        │     └─→ CSS 变量级联自动切换全部颜色
        └─→ localStorage.setItem("echoplay-theme", mode)
              └─→ 下次访问自动恢复
```

### 3.3 未来 API 调用流程（设计）

```
组件 dispatch → Store.fetchVideos()
  └─→ videoService.loadRecommendVideo()
        └─→ api.get<VideoItem[]>("/video/loadRecommendVideo")
              └─→ fetch (credentials: "include")
                    └─→ Cookie 自动携带 token
                          └─→ 后端验证 → 返回数据
                                └─→ Store 更新 → 响应式触发 UI 重渲染
```

---

## 4. 组件设计原则

### 4.1 Props 类型化

```typescript
// ✅ 类型声明 + withDefaults
interface VideoCardProps {
  video: VideoItem;
}
const props = withDefaults(defineProps<VideoCardProps>(), {});

// ❌ 运行时声明
const props = defineProps({
  video: { type: Object, required: true }
});
```

### 4.2 组合优于配置

```vue
<!-- ✅ Slot 组合 — 灵活、语义化 -->
<Card>
  <template #header><h2>标题</h2></template>
  <p>内容</p>
  <template #footer><button>操作</button></template>
</Card>

<!-- ❌ Props 配置 — 僵化、不可扩展 -->
<Card title="标题" content="内容" :footer-action="action" />
```

### 4.3 泛型组件

```vue
<script setup lang="ts" generic="T extends { id: string }">
interface Props {
  items: T[];
}
const props = defineProps<Props>();
</script>
```

---

## 5. 状态管理策略

### 状态分类

| 状态类型 | 解决方案 | 示例 |
|---------|---------|------|
| 局部 UI 状态 | `ref` / `reactive` | isOpen, form data |
| 跨组件共享 | Pinia Store | videos[], theme mode |
| 服务端数据 | Pinia + Service | 视频列表、用户信息 |
| URL 状态 | Router query params | 搜索关键词、分页 |
| 持久化状态 | localStorage + Pinia | 主题偏好 |

### 为什么 Pinia 而非 Vuex

1. **TypeScript 原生支持**：无需额外的类型声明文件，Store 的每个属性自动推导
2. **模块化天然**：Composition API 风格，每个 Store 独立，无需 `modules` 嵌套
3. **无 mutations**：直接 `state.value = newValue`，减少样板代码
4. **体积更小**：Pinia 约 1KB gzip，Vuex 4 约 5KB gzip

---

## 6. 主题系统设计

### 6.1 技术方案：CSS 自定义属性

选择纯 CSS 变量方案而非 CSS-in-JS 运行时：

```
优点：
  ✅ 零运行时开销 — 浏览器原生支持
  ✅ 切换瞬时 — data-theme 属性更新无重绘
  ✅ 级联自动 — 子元素自动继承
  ✅ 调试友好 — DevTools 可直接查看/修改

缺点：
  ⚠️ 无类型检查 — 通过命名约定和 tokens.css 集中管理缓解
  ⚠️ IE 不支持 — 目标用户为现代浏览器，不在支持范围
```

### 6.2 Token 体系

```
tokens.css
├── 色彩体系（~40 个变量）
│   ├── Brand: --color-primary / --color-accent
│   ├── Text: --color-text-primary / secondary / placeholder / inverse
│   ├── Background: --color-bg-page / container / hover / card / overlay
│   └── Border: --color-border / --color-border-light
├── 阴影体系（5 级）
│   └── --shadow-sm / md / lg / card / header
├── 排版体系（12 个变量）
│   └── font-family / font-size(8级) / font-weight(4级) / line-height(3级)
├── 间距体系（10 级，4px 基准）
│   └── --space-1(4px) → --space-12(48px)
├── 圆角体系（5 级）
│   └── --radius-sm(4px) → --radius-full(9999px)
└── 动效体系
    └── duration(3级) / ease(4级)
```

### 6.3 双主题切换

```css
/* 浅色（默认） */
:root, [data-theme="light"] {
  --color-primary: #1A6DF5;
  --color-bg-page: #F4F5F7;
}

/* 深色 */
[data-theme="dark"] {
  --color-primary: #4D94FF;     /* 深色下调亮主色以保证可读性 */
  --color-bg-page: #0D1117;
}
```

组件中只使用语义 token，不判断当前主题：
```css
/* ✅ 正确 */
.card { background: var(--color-bg-container); }

/* ❌ 错误 — 不要硬编码颜色 */
.card { background: #FFFFFF; }
```

### 6.4 系统主题跟随

```typescript
// 无用户手动设置时，自动跟随系统
const mql = window.matchMedia("(prefers-color-scheme: dark)");
mql.addEventListener("change", (e) => {
  if (getStoredTheme() === null) {
    mode.value = e.matches ? "dark" : "light";
  }
});
```

---

## 7. 路由设计

```typescript
const routes = [
  { path: "/", name: "home", component: () => import("@/views/HomeView.vue") },
  // 计划中：
  // { path: "/video", name: "video", component: () => import("@/views/VideoView.vue") },
  // { path: "/search", name: "search", component: () => import("@/views/SearchView.vue") },
  // { path: "/uhome", name: "uhome", component: () => import("@/views/UhomeView.vue") },
];

// 路由守卫：自动设置页面标题
router.beforeEach((to) => {
  document.title = (to.meta.title as string) || "EchoPlay";
});
```

**设计决策**：
- 所有页面使用**动态 import 懒加载**，首屏仅加载 HomeView
- `scrollBehavior` 返回 `{ top: 0 }`，切换页面自动回到顶部

---

## 8. 性能优化

### 已实施

| 措施 | 位置 | 效果 |
|------|------|------|
| CSS 变量主题 | `tokens.css` | 零运行时主题切换 |
| 路由懒加载 | `router/index.ts` | 首屏 JS 减小 |
| 图片懒加载 | `<img loading="lazy">` | 减少首屏带宽 |
| `font-variant-numeric: tabular-nums` | `VideoCard.vue` | 数字跳动不位移 |
| `prefers-reduced-motion` | `tokens.css` | 尊重用户动效偏好 |
| 组件级 CSS Scoped | 所有 `.vue` 文件 | 样式隔离，无冲突 |

### 计划中

| 措施 | 预期效果 |
|------|---------|
| `shallowRef` 大列表 | 1000+ 视频列表减少响应式追踪开销 |
| `v-memo` 视频卡片 | 仅在 `video.id` 变化时重渲染 |
| `defineAsyncComponent` | 重组件独立 chunk 加载 |
| 虚拟滚动 | 超长列表（如搜索结果）内存优化 |

---

## 9. 安全设计

| 措施 | 实现 |
|------|------|
| Cookie 认证 | `credentials: "include"` 自动携带，HttpOnly 防 XSS |
| 输入转义 | Vue 模板自动转义 `{{ }}` 插值 |
| 外部链接 | 待加 `rel="noopener noreferrer"` |
| HTML 内容 | 用户生成内容（视频简介、评论）展示前需 DOMPurify 清洗 |
| API 参数 | form-urlencoded 编码，URLSearchParams 自动编码 |

---

## 10. 无障碍设计

| 措施 | 示例 |
|------|------|
| 语义化 HTML | `<header>`, `<nav>`, `<main>`, `<section>`, `<article>` |
| ARIA 标签 | `aria-label`, `aria-labelledby`, `aria-current`, `role` |
| 键盘操作 | 轮播箭头可 Tab 聚焦，Search 回车提交 |
| 屏幕阅读器 | `.sr-only` 隐藏文本 |
| 焦点可见 | `:focus-visible` 2px 蓝色轮廓 |
| 减少动效 | `@media (prefers-reduced-motion)` 全局禁用动效 |

---

## 11. 响应式策略

采用**移动优先**的渐进增强：

| 断点 | 视频网格列数 | 侧边栏 | 导航标签 |
|------|:---------:|:-----:|:------:|
| > 1280px | 4 列 | 可见 | 可见 |
| 900-1280px | 3 列 | 可见 | 可见 |
| 640-900px | 2 列 | 隐藏 | 可见 |
| < 640px | 1 列 | 隐藏 | 隐藏（仅搜索+头像） |

```css
.video-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-6);
}

@media (max-width: 1280px) { .video-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 900px)  { .video-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 540px)  { .video-grid { grid-template-columns: 1fr; } }
```

---

## 12. 工程规范

### TypeScript

- 严格模式：`noUnusedLocals: true`, `noUnusedParameters: true`
- 禁止 `any` — 使用 `unknown` + 类型守卫
- Props/Emits 使用类型声明 `defineProps<T>()`
- 类型定义集中在 `types/index.ts`

### CSS

- 禁止硬编码颜色/间距 — 统一使用 `var(--token-name)`
- 使用 `@layer` 管理样式优先级（计划中）
- 组件级样式使用 `<style scoped>`
- 不使用 `!important`（除 utility 类）
- 禁止 `transition: all`

### Git

- Commit message 使用中文（与项目历史一致）
- 一个 Commit 做一件事
- `.env` 文件不入库（`.gitignore` 已配置）

---

## 13. 技术决策记录 (ADR)

### ADR-001：为什么不用 Tailwind CSS

**背景**：需要选择 CSS 方案。

**决策**：使用纯 CSS 自定义属性 + `<style scoped>`。

**理由**：
1. 设计系统需要双主题切换，CSS 变量是浏览器原生方案，Tailwind 的 `dark:` 前缀需要额外配置
2. 团队规模小（1 人），不需要 Tailwind 的"统一语言"降低沟通成本
3. `tokens.css` 140 个变量 + `@layer` 可达到同等原子化效果
4. Tailwind 的 `@apply` 在组件提取场景中是反模式，不如直接写组件

### ADR-002：为什么 Service 层用 form-urlencoded

**背景**：前端需要与 Spring Boot 后端通信。

**决策**：使用 `application/x-www-form-urlencoded` 而非 `application/json`。

**理由**：
1. 后端 Controller 使用 `@RequestParam` 接收参数（Spring Boot 默认行为）
2. 不修改后端代码即可完成对接
3. `URLSearchParams` 自动处理编码
4. 后续可按模块逐步迁移到 JSON（Content-Type 协商）

### ADR-003：为什么用 Pinia 而非 Vuex

**背景**：需要状态管理方案。

**决策**：使用 Pinia。

**理由**：
1. Vue 官方推荐（Vuex 5 合并入 Pinia）
2. 完整 TypeScript 类型推导
3. Composition API 风格，与 Vue 3 心智模型一致
4. 无 mutations 样板，代码量减少约 40%

### ADR-004：Mock 数据策略

**背景**：前端需要独立于后端开发。

**决策**：Store 初始化时加载 mock 数据，API 调用成功后替换。

**理由**：
1. 新加入的开发者无需配置后端即可运行前端
2. UI 开发不阻塞于 API 就绪
3. 通过 Service 抽象层，切换真实 API 只需改一行 import
4. Mock 数据结构与后端实体完全对齐（从 `Video.java` PO 推导）

---

## 14. 待改进项

### 短期（1-2 周）
- [ ] 视频详情页 + HTML5 播放器组件
- [ ] 登录/注册页面
- [ ] Vite proxy 配置 → 前后端联调
- [ ] 搜索结果页
- [ ] 全局 Loading/Error 状态组件

### 中期（1 个月）
- [ ] 虚拟滚动（视频搜索结果超长列表优化）
- [ ] 用户主页 + 创作中心
- [ ] 弹幕渲染引擎（Canvas 2D）
- [ ] `shallowRef` 优化大列表性能
- [ ] 图片懒加载 Intersection Observer polyfill

### 长期（2-3 个月）
- [ ] 国际化 i18n
- [ ] E2E 测试（Playwright）
- [ ] 单元测试（Vitest + Vue Test Utils）
- [ ] PWA 离线缓存
- [ ] 性能监控（Web Vitals + 自定义埋点）
- [ ] 移动端 H5 适配增强
