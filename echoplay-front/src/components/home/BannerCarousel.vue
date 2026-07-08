<script setup lang="ts">
import { ref, onMounted, onUnmounted } from "vue";
import type { BannerItem } from "@/types";

const props = defineProps<{
  banners: BannerItem[];
}>();

const current = ref(0);
let timer: ReturnType<typeof setInterval> | null = null;

function goTo(index: number) {
  const total = props.banners.length;
  if (total === 0) return;
  current.value = ((index % total) + total) % total;
}

function goNext() {
  goTo(current.value + 1);
}

function goPrev() {
  goTo(current.value - 1);
}

function startAutoPlay() {
  stopAutoPlay();
  if (props.banners.length > 1) {
    timer = setInterval(goNext, 5000);
  }
}

function stopAutoPlay() {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
}

onMounted(startAutoPlay);
onUnmounted(stopAutoPlay);
</script>

<template>
  <section
    class="banner-carousel"
    aria-label="推荐内容轮播"
    role="region"
    @mouseenter="stopAutoPlay"
    @mouseleave="startAutoPlay"
  >
    <!-- Slides -->
    <div class="banner-track">
      <a
        v-for="(banner, index) in banners"
        :key="banner.id"
        :href="banner.link"
        class="banner-slide"
        :class="{ active: index === current }"
        :aria-current="index === current ? 'true' : undefined"
      >
        <img
          :src="banner.imageUrl"
          :alt="banner.title"
          class="banner-image"
          width="1200"
          height="400"
          loading="lazy"
        />
        <div class="banner-overlay">
          <h2 class="banner-title">{{ banner.title }}</h2>
        </div>
      </a>
    </div>

    <!-- Arrows -->
    <button
      v-if="banners.length > 1"
      class="banner-arrow banner-arrow--left"
      aria-label="上一张"
      @click="goPrev"
    >
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
        <polyline points="15 18 9 12 15 6" />
      </svg>
    </button>
    <button
      v-if="banners.length > 1"
      class="banner-arrow banner-arrow--right"
      aria-label="下一张"
      @click="goNext"
    >
      <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
        <polyline points="9 18 15 12 9 6" />
      </svg>
    </button>

    <!-- Dots -->
    <div v-if="banners.length > 1" class="banner-dots" role="tablist" aria-label="轮播导航">
      <button
        v-for="(_, index) in banners"
        :key="index"
        class="banner-dot"
        :class="{ active: index === current }"
        role="tab"
        :aria-selected="index === current"
        :aria-label="`第 ${index + 1} 张`"
        @click="goTo(index)"
      />
    </div>
  </section>
</template>

<style scoped>
.banner-carousel {
  position: relative;
  border-radius: var(--radius-lg);
  overflow: hidden;
  aspect-ratio: 3 / 1;
  max-height: 360px;
  background: var(--color-bg-container);
  margin-bottom: var(--space-8);
  box-shadow: var(--shadow-md);
}

.banner-track {
  position: relative;
  width: 100%;
  height: 100%;
}

.banner-slide {
  position: absolute;
  inset: 0;
  opacity: 0;
  transition: opacity var(--duration-slow) var(--ease-out);
  pointer-events: none;
}

.banner-slide.active {
  opacity: 1;
  pointer-events: auto;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: var(--space-8) var(--space-6) var(--space-6);
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.65));
}

.banner-title {
  color: white;
  font-size: var(--text-xl);
  font-weight: var(--font-weight-semibold);
  line-height: var(--leading-tight);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
  max-width: 80%;
}

/* ── Arrows ── */
.banner-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  background: var(--color-bg-container);
  color: var(--color-text-primary);
  box-shadow: var(--shadow-sm);
  opacity: 0;
  transition: opacity var(--duration-fast) var(--ease-default),
              background-color var(--duration-fast) var(--ease-default);
}

.banner-carousel:hover .banner-arrow {
  opacity: 1;
}

.banner-arrow--left { left: var(--space-4); }
.banner-arrow--right { right: var(--space-4); }

.banner-arrow:hover {
  background: var(--color-bg-container);
}

/* ── Dots ── */
.banner-dots {
  position: absolute;
  bottom: var(--space-3);
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: var(--space-2);
}

.banner-dot {
  width: 8px;
  height: 8px;
  border-radius: var(--radius-full);
  background: rgba(255, 255, 255, 0.5);
  padding: 0;
  border: none;
  cursor: pointer;
  transition: background-color var(--duration-fast) var(--ease-default),
              transform var(--duration-fast) var(--ease-bounce);
}

.banner-dot.active {
  background: var(--color-primary);
  transform: scale(1.5);
}

.banner-dot:hover {
  background: rgba(255, 255, 255, 0.8);
}
</style>
