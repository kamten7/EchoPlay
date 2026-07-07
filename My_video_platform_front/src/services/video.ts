import { api } from "./core";
import type { VideoItem, PaginationResult } from "@/types";

export const videoService = {
  /** 加载推荐视频 */
  loadRecommendVideo() {
    return api.get<VideoItem[]>("/video/loadRecommendVideo");
  },

  /** 分页加载视频列表 */
  loadVideo(params: {
    pCategoryId?: number;
    categoryId?: number;
    pageNo: number;
  }) {
    return api.get<PaginationResult<VideoItem>>("/video/loadVideo", params as Record<string, string | number | undefined>);
  },

  /** 获取视频详情 */
  getVideoInfo(videoId: string) {
    return api.get<VideoItem>("/video/getVideoInfo", { videoId });
  },

  /** 搜索视频 */
  search(keyword: string, pageNo: number) {
    return api.get<PaginationResult<VideoItem>>("/video/search", { keyword, pageNo });
  },

  /** 热搜关键词 */
  getSearchKeywordTop() {
    return api.get<string[]>("/video/getSearchKeywordTop");
  },

  /** 视频推荐（侧栏） */
  getVideoRecommend() {
    return api.get<VideoItem[]>("/video/getVideoRecommend");
  },

  /** 热门视频 */
  loadHotVideoList() {
    return api.get<VideoItem[]>("/video/loadHotVideoList");
  },
};
