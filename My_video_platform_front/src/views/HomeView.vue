<script setup lang="ts">
import AppLayout from "@/components/layout/AppLayout.vue";
import BannerCarousel from "@/components/home/BannerCarousel.vue";
import VideoGrid from "@/components/home/VideoGrid.vue";
import { useVideoStore } from "@/stores/video";
import { storeToRefs } from "pinia";

const store = useVideoStore();
const { banners, recommendVideos, hotVideos, recentVideos } = storeToRefs(store);

function handleSearch(keyword: string) {
  // Navigate to search results — will be wired to router later
  console.log("Search:", keyword);
}
</script>

<template>
  <AppLayout @search="handleSearch">
    <div class="home-page">
      <!-- Banner Carousel -->
      <BannerCarousel :banners="banners" />

      <!-- Recommended (pinned) -->
      <VideoGrid
        v-if="recommendVideos.length"
        title="🎯 编辑推荐"
        :videos="recommendVideos"
        more-link="/recommend"
      />

      <!-- Hot Videos -->
      <VideoGrid
        title="🔥 热门视频"
        :videos="hotVideos.slice(0, 12)"
        more-link="/hot"
      />

      <!-- Recent Videos -->
      <VideoGrid
        title="🆕 最新发布"
        :videos="recentVideos.slice(0, 12)"
        more-link="/new"
      />
    </div>
  </AppLayout>
</template>

<style scoped>
.home-page {
  max-width: 100%;
}
</style>
