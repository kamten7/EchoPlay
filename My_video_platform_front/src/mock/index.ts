import type { BannerItem, VideoItem } from "@/types";

/**
 * Mock 数据 — 首屏占位用。
 * 后端 API 就绪后，services 会自动替换掉这些数据。
 */

export const mockBanners: BannerItem[] = [
  {
    id: "b1",
    imageUrl: "https://picsum.photos/seed/bilibili1/1200/400",
    title: "【年度巨献】2026 动画大赏 开启投票！",
    link: "/search?keyword=动画大赏",
  },
  {
    id: "b2",
    imageUrl: "https://picsum.photos/seed/bilibili2/1200/400",
    title: "《鬼灭之刃 最终季》连载中",
    link: "/video?v=1",
  },
  {
    id: "b3",
    imageUrl: "https://picsum.photos/seed/bilibili3/1200/400",
    title: "Minecraft 史诗建筑大赛 作品展示",
    link: "/video?v=2",
  },
  {
    id: "b4",
    imageUrl: "https://picsum.photos/seed/bilibili4/1200/400",
    title: "独立游戏创作马拉松 佳作合集",
    link: "/search?keyword=独立游戏",
  },
];

export const mockVideos: VideoItem[] = Array.from({ length: 24 }, (_, i) => ({
  id: i + 1,
  videoId: `v_${String(i + 1).padStart(8, "0")}`,
  userId: `user_${(i % 8) + 1}`,
  nickName: ["老番茄", "某幻君", "中国BOY", "逍遥散人", "敖厂长", "籽岷", "籽岷", "黑桐谷歌"][i % 8],
  avatar: `https://picsum.photos/seed/avatar${i}/64/64`,
  fileId: `file_${i + 1}`,
  videoName: [
    "【实况】这是我玩过最离谱的恐怖游戏！",
    "一口气看完《三体》三部曲 精讲",
    "2026 春季新番盘点 这些番剧值得追",
    "【翻唱】Tabi no Tochuu / 狼与香辛料 OP",
    "用 100 天在 Minecraft 里建造一座城市",
    "Python 入门教程 第 1 集：环境搭建与 Hello World",
    "最近爆火的独立游戏《幻兽帕鲁Plus》试玩",
    "【纪录片】我在南极科考站的一百天",
    "【Vlog】东京动漫展 Day 1 全程记录",
    "手把手教你搭建自己的视频网站",
    "《塞尔达传说》全神庙收集攻略",
    "【搞笑】当老爸第一次玩 VR 游戏",
    "数码评测：2026 年最值得买的 5 款降噪耳机",
    "【美食】深夜放毒 — 在家复刻一兰拉面",
    "健身小白入门指南：从 0 到第一个引体向上",
    "《原神》4.8 版本新地图全解谜",
    "【科普】量子计算机到底有多强大？",
    "【绘画】Procreate 厚涂技巧分享",
    "经典电影重映！《千与千寻》4K 修复版",
    "【旅行】日本九州 7 天 6 夜自由行攻略",
    "【杂谈】B站十年，我的 UP 主之路",
    "【鬼畜】全明星 Rap Battle 2026",
    "【教程】Figma 入门：设计师必备工具",
    "《艾尔登法环 2》实机演示 全 BOSS 战",
  ][i % 24],
  videoCover: `https://picsum.photos/seed/cover${i}/480/270`,
  categoryId: (i % 10) + 1,
  pCategoryId: (i % 5) + 1,
  postType: 1,
  tags: ["推荐", "热门"].join(","),
  introduction: "精彩视频内容",
  duration: Math.floor(Math.random() * 3600) + 120,
  playCount: Math.floor(Math.random() * 500000) + 1000,
  likeCount: Math.floor(Math.random() * 50000) + 100,
  coinCount: Math.floor(Math.random() * 10000) + 50,
  collectCount: Math.floor(Math.random() * 20000) + 200,
  commentCount: Math.floor(Math.random() * 5000) + 10,
  danmuCount: Math.floor(Math.random() * 10000) + 50,
  shareCount: Math.floor(Math.random() * 3000) + 20,
  status: 3,
  recommendType: i < 4 ? 1 : 0,
  postTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toISOString(),
}));
