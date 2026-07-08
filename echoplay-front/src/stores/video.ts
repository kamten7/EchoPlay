import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type { VideoItem, BannerItem } from "@/types";
import { mockVideos, mockBanners } from "@/mock";

export const useVideoStore = defineStore("video", () => {
  const videos = ref<VideoItem[]>(mockVideos);
  const banners = ref<BannerItem[]>(mockBanners);
  const loading = ref(false);

  const recommendVideos = computed(() =>
    videos.value.filter((v) => v.recommendType === 1)
  );

  const hotVideos = computed(() =>
    [...videos.value].sort((a, b) => b.playCount - a.playCount)
  );

  const recentVideos = computed(() =>
    [...videos.value].sort(
      (a, b) => new Date(b.postTime).getTime() - new Date(a.postTime).getTime()
    )
  );

  function setVideos(list: VideoItem[]) {
    videos.value = list;
  }

  function setBanners(list: BannerItem[]) {
    banners.value = list;
  }

  return {
    videos,
    banners,
    loading,
    recommendVideos,
    hotVideos,
    recentVideos,
    setVideos,
    setBanners,
  };
});
