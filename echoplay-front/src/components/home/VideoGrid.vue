<script setup lang="ts">
import VideoCard from "./VideoCard.vue";
import type { VideoItem } from "@/types";

defineProps<{
  title: string;
  videos: VideoItem[];
  moreLink?: string;
}>();
</script>

<template>
  <section class="video-grid-section" aria-labelledby="section-title">
    <header class="section-header">
      <h2 class="section-title">{{ title }}</h2>
      <a v-if="moreLink" :href="moreLink" class="section-more">
        查看更多
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
          <polyline points="9 18 15 12 9 6" />
        </svg>
      </a>
    </header>

    <div class="video-grid">
      <VideoCard v-for="video in videos" :key="video.videoId" :video="video" />
    </div>
  </section>
</template>

<style scoped>
.video-grid-section {
  margin-bottom: var(--space-8);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: var(--space-4);
}

.section-title {
  font-size: var(--text-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--color-text-primary);
  letter-spacing: -0.02em;
}

.section-more {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-sm);
  color: var(--color-text-placeholder);
  padding: var(--space-1) var(--space-3);
  border-radius: var(--radius-full);
  background: var(--color-bg-container);
  border: 1px solid var(--color-border);
  transition: color var(--duration-fast) var(--ease-default),
              background-color var(--duration-fast) var(--ease-default),
              border-color var(--duration-fast) var(--ease-default);
}

.section-more:hover {
  color: var(--color-primary);
  border-color: var(--color-primary);
  background: var(--color-primary-light);
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-6);
}

@media (max-width: 1280px) {
  .video-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 900px) {
  .video-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: var(--space-4);
  }
}

@media (max-width: 540px) {
  .video-grid {
    grid-template-columns: 1fr;
  }
}
</style>
