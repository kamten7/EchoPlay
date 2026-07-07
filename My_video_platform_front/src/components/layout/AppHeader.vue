<script setup lang="ts">
import { ref } from "vue";
import { useThemeStore } from "@/stores/theme";

interface Tab {
  label: string;
  path: string;
}

const tabs: Tab[] = [
  { label: "首页", path: "/" },
  { label: "番剧", path: "/anime" },
  { label: "直播", path: "/live" },
  { label: "游戏", path: "/game" },
  { label: "知识", path: "/knowledge" },
  { label: "音乐", path: "/music" },
  { label: "专栏", path: "/column" },
];

const searchKeyword = ref("");
const emit = defineEmits<{
  search: [keyword: string];
}>();

const themeStore = useThemeStore();

function handleSearch() {
  if (searchKeyword.value.trim()) {
    emit("search", searchKeyword.value.trim());
  }
}

function handleKeydown(e: KeyboardEvent) {
  if (e.key === "Enter") {
    handleSearch();
  }
}
</script>

<template>
  <header class="app-header" role="banner">
    <div class="header-inner">
      <!-- Logo -->
      <a href="/" class="header-logo" aria-label="EchoPlay 首页">
        <span class="logo-icon">K</span>
        <span class="logo-text">EchoPlay</span>
      </a>

      <!-- Nav Tabs -->
      <nav class="header-nav" aria-label="主导航">
        <a
          v-for="tab in tabs"
          :key="tab.label"
          :href="tab.path"
          class="nav-link"
          :class="{ active: tab.path === '/' }"
        >
          {{ tab.label }}
        </a>
      </nav>

      <!-- Search -->
      <div class="header-search" role="search">
        <form @submit.prevent="handleSearch" class="search-form">
          <label for="global-search" class="sr-only">搜索视频</label>
          <input
            id="global-search"
            v-model="searchKeyword"
            type="search"
            class="search-input"
            placeholder="搜索你感兴趣的内容..."
            autocomplete="off"
            @keydown="handleKeydown"
          />
          <button
            type="submit"
            class="search-btn"
            aria-label="搜索"
          >
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
              <circle cx="11" cy="11" r="8" />
              <path d="m21 21-4.35-4.35" />
            </svg>
          </button>
        </form>
      </div>

      <!-- User Area -->
      <div class="header-actions">
        <button class="action-btn" aria-label="观看历史">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
            <circle cx="12" cy="12" r="10" />
            <polyline points="12 6 12 12 16 14" />
          </svg>
        </button>

        <!-- Theme Toggle -->
        <button
          class="action-btn"
          :aria-label="themeStore.mode === 'dark' ? '切换到浅色模式' : '切换到深色模式'"
          :title="themeStore.mode === 'dark' ? '浅色模式' : '深色模式'"
          @click="themeStore.toggle()"
        >
          <!-- Sun icon (dark → switch to light) -->
          <svg
            v-if="themeStore.mode === 'dark'"
            width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"
          >
            <circle cx="12" cy="12" r="5" />
            <line x1="12" y1="1" x2="12" y2="3" />
            <line x1="12" y1="21" x2="12" y2="23" />
            <line x1="4.22" y1="4.22" x2="5.64" y2="5.64" />
            <line x1="18.36" y1="18.36" x2="19.78" y2="19.78" />
            <line x1="1" y1="12" x2="3" y2="12" />
            <line x1="21" y1="12" x2="23" y2="12" />
            <line x1="4.22" y1="19.78" x2="5.64" y2="18.36" />
            <line x1="18.36" y1="5.64" x2="19.78" y2="4.22" />
          </svg>
          <!-- Moon icon (light → switch to dark) -->
          <svg
            v-else
            width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true"
          >
            <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
          </svg>
        </button>

        <button class="action-btn action-btn--upload" aria-label="上传视频">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
            <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
            <polyline points="17 8 12 3 7 8" />
            <line x1="12" x2="12" y1="3" y2="15" />
          </svg>
          <span class="upload-text">投稿</span>
        </button>

        <button class="action-btn notification-btn" aria-label="消息通知">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
            <path d="M6 8a6 6 0 0 1 12 0c0 7 3 9 3 9H3s3-2 3-9" />
            <path d="M10.3 21a1.94 1.94 0 0 0 3.4 0" />
          </svg>
          <span class="badge">3</span>
        </button>

        <button class="action-btn avatar-btn" aria-label="个人中心">
          <img
            src="https://picsum.photos/seed/user-avatar/64/64"
            alt="用户头像"
            class="user-avatar"
            width="32"
            height="32"
            loading="lazy"
          />
        </button>
      </div>
    </div>
  </header>
</template>

<style scoped>
.app-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  height: var(--header-height);
  background: var(--header-bg);
  border-bottom: 1px solid var(--color-border);
  box-shadow: var(--shadow-header);
  transition: background-color var(--duration-normal) var(--ease-default),
              border-color var(--duration-normal) var(--ease-default);
}

.header-inner {
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: 0 var(--space-6);
  height: 100%;
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

/* ── Logo ── */
.header-logo {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  color: var(--color-primary);
  font-weight: var(--font-weight-bold);
  font-size: var(--text-xl);
  flex-shrink: 0;
  text-decoration: none;
  transition: opacity var(--duration-fast) var(--ease-default);
}

.header-logo:hover {
  opacity: 0.8;
}

.logo-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: var(--color-primary);
  color: white;
  border-radius: var(--radius-md);
  font-size: 1.125rem;
  font-weight: var(--font-weight-bold);
}

.logo-text {
  font-size: var(--text-lg);
  color: var(--header-text);
  letter-spacing: -0.02em;
}

/* ── Nav ── */
.header-nav {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  margin-left: var(--space-4);
}

.nav-link {
  padding: var(--space-1) var(--space-3);
  color: var(--color-text-secondary);
  font-size: var(--text-base);
  font-weight: var(--font-weight-medium);
  border-radius: var(--radius-sm);
  transition: color var(--duration-fast) var(--ease-default),
              background-color var(--duration-fast) var(--ease-default);
  white-space: nowrap;
}

.nav-link:hover {
  color: var(--color-primary);
  background-color: var(--color-primary-ghost);
}

.nav-link.active {
  color: var(--color-primary);
  background-color: var(--color-primary-light);
  font-weight: var(--font-weight-semibold);
}

/* ── Search ── */
.header-search {
  flex: 1;
  max-width: 480px;
  margin: 0 auto;
}

.search-form {
  display: flex;
  align-items: center;
  background: var(--header-search-bg);
  border: 1px solid var(--header-search-border);
  border-radius: var(--radius-full);
  overflow: hidden;
  transition: background-color var(--duration-fast) var(--ease-default),
              border-color var(--duration-fast) var(--ease-default);
}

.search-form:focus-within {
  background: var(--header-search-focus-bg);
  border-color: var(--header-search-focus-border);
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  padding: var(--space-2) var(--space-4);
  color: var(--header-search-text);
  font-size: var(--text-base);
  outline: none;
  min-width: 0;
}

.search-input::placeholder {
  color: var(--header-search-placeholder);
}

.search-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--space-2) var(--space-4);
  color: var(--color-text-placeholder);
  transition: color var(--duration-fast) var(--ease-default);
}

.search-form:focus-within .search-btn {
  color: var(--color-primary);
}

/* ── Actions ── */
.header-actions {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  flex-shrink: 0;
}

.action-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  color: var(--header-action-color);
  border-radius: var(--radius-full);
  transition: background-color var(--duration-fast) var(--ease-default),
              color var(--duration-fast) var(--ease-default);
}

.action-btn:hover {
  background-color: var(--header-action-hover);
  color: var(--color-primary);
}

.action-btn--upload {
  width: auto;
  padding: 0 var(--space-3);
  gap: var(--space-1);
  background: var(--header-upload-bg);
  color: var(--header-upload-color);
}

.action-btn--upload:hover {
  background: var(--header-upload-hover-bg);
  color: var(--color-primary);
}

.upload-text {
  font-size: var(--text-sm);
  font-weight: var(--font-weight-medium);
}

.badge {
  position: absolute;
  top: 2px;
  right: 2px;
  min-width: 16px;
  height: 16px;
  padding: 0 4px;
  background: var(--color-primary);
  color: white;
  font-size: 0.625rem;
  font-weight: var(--font-weight-bold);
  line-height: 16px;
  text-align: center;
  border-radius: var(--radius-full);
}

.avatar-btn {
  width: 32px;
  height: 32px;
  padding: 0;
  overflow: hidden;
  border: 2px solid var(--color-border);
  border-radius: var(--radius-full);
  transition: border-color var(--duration-fast) var(--ease-default);
}

.avatar-btn:hover {
  border-color: var(--color-primary);
}

.user-avatar {
  width: 100%;
  height: 100%;
  border-radius: var(--radius-full);
  object-fit: cover;
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .header-nav {
    display: none;
  }

  .header-search {
    max-width: none;
  }
}

@media (max-width: 640px) {
  .header-actions .action-btn:not(.avatar-btn):not(.theme-toggle) {
    display: none;
  }
}
</style>
