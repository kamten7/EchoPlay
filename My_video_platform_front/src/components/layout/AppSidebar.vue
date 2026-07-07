<script setup lang="ts">
interface SidebarItem {
  label: string;
  icon: string;
  path: string;
  active?: boolean;
}

const sidebarItems: SidebarItem[] = [
  { label: "热门", icon: "🔥", path: "/hot", active: true },
  { label: "动画", icon: "🎬", path: "/anime" },
  { label: "游戏", icon: "🎮", path: "/game" },
  { label: "音乐", icon: "🎵", path: "/music" },
  { label: "知识", icon: "📚", path: "/knowledge" },
  { label: "科技", icon: "💻", path: "/tech" },
  { label: "生活", icon: "🏠", path: "/life" },
  { label: "美食", icon: "🍜", path: "/food" },
  { label: "影视", icon: "🎥", path: "/movie" },
  { label: "舞蹈", icon: "💃", path: "/dance" },
  { label: "时尚", icon: "👗", path: "/fashion" },
];

interface HotTopic {
  label: string;
  heat: string;
}

const hotTopics: HotTopic[] = [
  { label: "#2026春季新番 盘点", heat: "128万" },
  { label: "#独游创作马拉松", heat: "96万" },
  { label: "#Python 入门教程", heat: "85万" },
  { label: "#Minecraft 史诗建造", heat: "72万" },
  { label: "#《幻兽帕鲁PLUS》", heat: "68万" },
];
</script>

<template>
  <aside class="app-sidebar" aria-label="侧边导航">
    <!-- 分类导航 -->
    <nav class="sidebar-section" aria-label="内容分类">
      <ul class="sidebar-nav">
        <li v-for="item in sidebarItems" :key="item.label">
          <a
            :href="item.path"
            class="sidebar-link"
            :class="{ active: item.active }"
          >
            <span class="sidebar-icon" aria-hidden="true">{{ item.icon }}</span>
            <span class="sidebar-label">{{ item.label }}</span>
          </a>
        </li>
      </ul>
    </nav>

    <!-- 热搜话题 -->
    <section class="sidebar-section" aria-labelledby="hot-topics-title">
      <h3 id="hot-topics-title" class="section-title">🔥 热门话题</h3>
      <ul class="hot-list">
        <li v-for="topic in hotTopics" :key="topic.label">
          <a href="#" class="hot-link">
            <span class="hot-label">{{ topic.label }}</span>
            <span class="hot-heat">{{ topic.heat }}热度</span>
          </a>
        </li>
      </ul>
    </section>

    <footer class="sidebar-footer">
      <a href="#" class="footer-link">关于我们</a>
      <a href="#" class="footer-link">用户协议</a>
      <a href="#" class="footer-link">隐私政策</a>
      <a href="#" class="footer-link">反馈建议</a>
    </footer>
  </aside>
</template>

<style scoped>
.app-sidebar {
  width: var(--sidebar-width);
  flex-shrink: 0;
  padding: var(--space-4) var(--space-3);
  overflow-y: auto;
  position: sticky;
  top: calc(var(--header-height) + var(--space-4));
  max-height: calc(100vh - var(--header-height) - var(--space-4));
}

.sidebar-section {
  margin-bottom: var(--space-6);
}

.section-title {
  font-size: var(--text-sm);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-secondary);
  margin-bottom: var(--space-3);
  padding-left: var(--space-2);
}

/* ── Nav Links ── */
.sidebar-link {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-md);
  font-size: var(--text-base);
  font-weight: var(--font-weight-medium);
  color: var(--color-text-primary);
  transition: background-color var(--duration-fast) var(--ease-default),
              color var(--duration-fast) var(--ease-default);
  margin-bottom: 2px;
}

.sidebar-link:hover {
  background-color: var(--color-bg-hover);
}

.sidebar-link.active {
  background-color: var(--color-primary-light);
  color: var(--color-primary);
  font-weight: var(--font-weight-semibold);
}

.sidebar-icon {
  font-size: 1.125rem;
  width: 24px;
  text-align: center;
  flex-shrink: 0;
}

.sidebar-label {
  white-space: nowrap;
}

/* ── Hot Topics ── */
.hot-link {
  display: flex;
  flex-direction: column;
  gap: 2px;
  padding: var(--space-2) var(--space-3);
  border-radius: var(--radius-md);
  transition: background-color var(--duration-fast) var(--ease-default);
}

.hot-link:hover {
  background-color: var(--color-bg-hover);
}

.hot-label {
  font-size: var(--text-sm);
  color: var(--color-text-primary);
  line-height: var(--leading-tight);
}

.hot-heat {
  font-size: var(--text-xs);
  color: var(--color-text-placeholder);
}

/* ── Footer ── */
.sidebar-footer {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-1) var(--space-3);
  padding: var(--space-3);
  border-top: 1px solid var(--color-border-light);
}

.footer-link {
  font-size: var(--text-xs);
  color: var(--color-text-placeholder);
  transition: color var(--duration-fast) var(--ease-default);
  white-space: nowrap;
}

.footer-link:hover {
  color: var(--color-text-secondary);
}

/* ── Responsive ── */
@media (max-width: 1024px) {
  .app-sidebar {
    display: none;
  }
}
</style>
