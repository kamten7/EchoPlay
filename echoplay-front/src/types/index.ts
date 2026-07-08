/** 视频数据（对应后端 Video PO） */
export interface VideoItem {
  id: number;
  videoId: string;
  userId: string;
  nickName?: string;
  avatar?: string;
  fileId?: string;
  videoName: string;
  videoCover: string;
  categoryId: number;
  pCategoryId: number;
  postType: number;
  tags: string;
  introduction?: string;
  duration: number;
  playCount: number;
  likeCount: number;
  coinCount: number;
  collectCount: number;
  commentCount: number;
  danmuCount: number;
  shareCount: number;
  status: number;
  recommendType: number;
  postTime: string;
  lastPlayTime?: string;
  createTime?: string;
}

/** 视频分P */
export interface VideoP {
  id: number;
  videoId: string;
  pName: string;
  fileId: string;
  duration?: number;
  status: number;
}

/** 分类数据 */
export interface CategoryItem {
  categoryId: number;
  pCategoryId: number;
  categoryCode: string;
  categoryName: string;
  icon?: string;
  background?: string;
  sort: number;
  children?: CategoryItem[];
}

/** 用户信息 */
export interface UserInfo {
  userId: string;
  nickName: string;
  email?: string;
  avatar?: string;
  sex: number;
  birthday?: string;
  school?: string;
  personIntroduction?: string;
  description?: string;
  joinTime?: string;
  lastLoginTime?: string;
  status: number;
  noticeInfo?: string;
  coinCount: number;
  currentCoinCount: number;
  theme: number;
  focusCount: number;
  fansCount: number;
  totalLikeCount: number;
}

/** 评论 */
export interface CommentItem {
  commentId: string;
  videoId: string;
  userId: string;
  content: string;
  imgPath?: string;
  replyCommentId?: string;
  replyUserId?: string;
  likeCount: number;
  topType: number;
  postTime: string;
  nickName?: string;
  avatar?: string;
}

/** Banner 轮播项 */
export interface BannerItem {
  id: string;
  imageUrl: string;
  title: string;
  link: string;
}

/** 导航菜单项 */
export interface NavItem {
  id: string;
  label: string;
  icon?: string;
  path: string;
}

/** API 分页结果 */
export interface PaginationResult<T> {
  pageInfo: PageInfo;
  list: T[];
}

export interface PageInfo {
  pageNo: number;
  pageSize: number;
  totalCount: number;
  pageTotal: number;
}

/** API 通用响应 */
export interface ApiResponse<T = unknown> {
  code: number;
  message: string;
  data: T;
}

/** 登录 Token 用户信息 */
export interface TokenUserInfo {
  userId: string;
  nickName: string;
  avatar?: string;
  token: string;
  expireTime?: number;
  coinCount?: number;
  currentCoinCount?: number;
  focusCount?: number;
  fansCount?: number;
}

/** 用户互动操作类型 */
export enum ActionType {
  LIKE = 1,
  COIN = 2,
  COLLECT = 3,
  SHARE = 4,
}

/** 视频状态 */
export enum VideoStatus {
  DRAFT = 0,
  REVIEWING = 1,
  REJECTED = 2,
  PUBLISHED = 3,
}
