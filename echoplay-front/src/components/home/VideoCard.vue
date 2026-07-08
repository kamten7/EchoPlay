<script setup lang="ts">
import type { VideoItem } from "@/types";

const props = defineProps<{
  video: VideoItem;
}>();

function formatCount(n: number): string {
  if (n >= 1_0000_0000) return `${(n / 1_0000_0000).toFixed(1)}亿`;
  if (n >= 10000) return `${(n / 10000).toFixed(1)}万`;
  return String(n);
}

function formatDuration(seconds: number): string {
  const m = Math.floor(seconds / 60);
  const s = seconds % 60;
  return `${String(m).padStart(2, "0")}:${String(s).padStart(2, "0")}`;
}

function timeAgo(dateStr: string): string {
  const diff = Date.now() - new Date(dateStr).getTime();
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / 86400000);

  if (minutes < 1) return "刚刚";
  if (minutes < 60) return `${minutes}分钟前`;
  if (hours < 24) return `${hours}小时前`;
  if (days < 30) return `${days}天前`;
  return new Date(dateStr).toLocaleDateString("zh-CN");
}
</script>

<template>
  <article class="video-card">
    <a :href="`/video?v=${video.videoId}`" class="video-card-link">
      <!-- Cover -->
      <div class="card-cover">
        <img
          :src="video.videoCover"
          :alt="video.videoName"
          class="cover-image"
          width="480"
          height="270"
          loading="lazy"
        />
        <span class="cover-duration">{{ formatDuration(video.duration) }}</span>
        <div class="cover-mask">
          <span class="mask-play-icon" aria-hidden="true">▶</span>
        </div>
      </div>

      <!-- Info -->
      <div class="card-info">
        <h3 class="card-title" :title="video.videoName">{{ video.videoName }}</h3>
        <div class="card-meta">
          <span class="meta-item" :title="`播放: ${video.playCount.toLocaleString()}`">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
              <path d="M8 5v14l11-7z"/>
            </svg>
            {{ formatCount(video.playCount) }}
          </span>
          <span class="meta-item" :title="`弹幕: ${video.danmuCount.toLocaleString()}`">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
              <rect x="2" y="4" width="20" height="3" rx="1.5"/>
              <rect x="4" y="10" width="16" height="3" rx="1.5"/>
              <rect x="1" y="16" width="18" height="3" rx="1.5"/>
            </svg>
            {{ formatCount(video.danmuCount) }}
          </span>
          <span class="meta-item meta-time">{{ timeAgo(video.postTime) }}</span>
        </div>
      </div>
    </a>

    <!-- Uploader -->
    <div class="card-uploader">
      <a :href="`/uhome?uid=${video.userId}`" class="uploader-link">
        <img
          :src="video.avatar || 'https://picsum.photos/seed/default/48/48'"
          :alt="video.nickName"
          class="uploader-avatar"
          width="24"
          height="24"
          loading="lazy"
        />
        <span class="uploader-name">{{ video.nickName }}</span>
      </a>
    </div>
  </article>
</template>

<style scoped>
.video-card {
  display: flex;
  flex-direction: column;
  transition: transform var(--duration-normal) var(--ease-bounce);
}

.video-card:hover {
  transform: var(--card-hover-transform);
}

.video-card-link {
  display: contents;
  color: inherit;
}

/* ── Cover ── */
.card-cover {
  position: relative;
  aspect-ratio: 16 / 9;
  border-radius: var(--radius-md);
  overflow: hidden;
  background: var(--color-bg-hover);
}

.cover-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform var(--duration-normal) var(--ease-out);
}

.video-card:hover .cover-image {
  transform: scale(1.05);
}

.cover-duration {
  position: absolute;
  bottom: var(--space-1);
  right: var(--space-1);
  padding: 1px 6px;
  background: rgba(0, 0, 0, 0.75);
  color: white;
  font-size: 0.6875rem;
  font-weight: var(--font-weight-medium);
  border-radius: var(--radius-sm);
  font-variant-numeric: tabular-nums;
}

.cover-mask {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity var(--duration-normal) var(--ease-default);
}

.video-card:hover .cover-mask {
  opacity: 1;
}

.mask-play-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 52px;
  height: 52px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  font-size: 1.5rem;
  border-radius: var(--radius-full);
  backdrop-filter: blur(4px);
}

/* ── Info ── */
.card-info {
  padding: var(--space-2) 0;
}

.card-title {
  font-size: var(--text-base);
  font-weight: var(--font-weight-medium);
  line-height: var(--leading-tight);
  color: var(--color-text-primary);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  margin-bottom: var(--space-1);
  min-height: calc(2 * var(--leading-tight) * var(--text-base));
  transition: color var(--duration-fast) var(--ease-default);
}

.card-title:hover {
  color: var(--color-primary);
}

.card-meta {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 2px;
  font-size: var(--text-xs);
  color: var(--color-text-placeholder);
}

.meta-time {
  margin-left: auto;
}

/* ── Uploader ── */
.card-uploader {
  padding-top: var(--space-1);
}

.uploader-link {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  font-size: var(--text-sm);
  color: var(--color-text-secondary);
  transition: color var(--duration-fast) var(--ease-default);
}

.uploader-link:hover {
  color: var(--color-primary);
}

.uploader-avatar {
  width: 20px;
  height: 20px;
  border-radius: var(--radius-full);
  object-fit: cover;
  flex-shrink: 0;
}

.uploader-name {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
