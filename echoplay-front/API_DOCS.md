# EchoPlay API 接口文档

> **Base URL**: `http://localhost:8080/api`（后端默认端口）  
> **Content-Type**: `application/x-www-form-urlencoded`（GET/POST 均使用）  
> **认证方式**: Cookie 中携带 `admin_token` / `token`  
> **响应格式**: `{ code: number, message: string, data: T }`

**状态说明**：✅ 已实现 | ⚠️ 部分实现 | ❌ 未实现

---

## 1. 账户模块 `/account`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | GET/POST | `/account/checkCode` | 获取图形验证码 | ✅ | ❌ |
| 2 | POST | `/account/register` | 用户注册 | ✅ | ❌ |
| 3 | POST | `/account/login` | 用户登录 | ✅ | ❌ |
| 4 | POST | `/account/autoLogin` | Cookie 自动登录 | ✅ | ❌ |
| 5 | POST | `/account/logout` | 退出登录 | ✅ | ❌ |
| 6 | GET/POST | `/account/getUserCountInfo` | 获取平台用户总数 | ✅ | ❌ |

**待开发前端页面**：登录页、注册页、验证码组件

<details>
<summary>接口详情</summary>

### 1.1 GET `/account/checkCode`
- **返回**: `{ checkCode: "base64图片", checkCodeKey: "redisKey" }`

### 1.2 POST `/account/register`
- **请求参数**: `email`(max150), `nickName`(max20), `registerPassword`(正则校验), `checkCodeKey`, `checkCode`

### 1.3 POST `/account/login`
- **请求参数**: `email`, `password`, `checkCodeKey`, `checkCode`
- **返回**: `TokenUserInfoDto { userId, nickName, avatar, token, coinCount, focusCount, fansCount, ... }`

### 1.4 POST `/account/autoLogin`
- **需要 Cookie**: `token`
- **返回**: `TokenUserInfoDto`

### 1.5 POST `/account/logout`
- **返回**: `null`

### 1.6 GET `/account/getUserCountInfo`
- **返回**: `number`（总用户数）
</details>

---

## 2. 视频模块 `/video`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | GET/POST | `/video/loadRecommendVideo` | 加载推荐视频列表 | ✅ | ⚠️ |
| 2 | GET/POST | `/video/loadVideo` | 分页加载视频（可按分类） | ✅ | ⚠️ |
| 3 | GET/POST | `/video/getVideoInfo` | 获取视频详情 | ✅ | ❌ |
| 4 | GET/POST | `/video/loadVideoPList` | 获取视频分P列表 | ✅ | ❌ |
| 5 | POST | `/video/reportVideoPlayOnline` | 上报在线播放心跳 | ✅ | ❌ |
| 6 | GET/POST | `/video/search` | 搜索视频 | ✅ | ❌ |
| 7 | GET/POST | `/video/getSearchKeywordTop` | 热搜关键词 Top10 | ✅ | ❌ |
| 8 | GET/POST | `/video/getVideoRecommend` | 侧栏视频推荐 | ✅ | ⚠️ |
| 9 | GET/POST | `/video/loadHotVideoList` | 热门视频列表 | ✅ | ⚠️ |

> ⚠️ **前端已实现 mock 数据降级**：首页使用 `mock/index.ts` 中的 24 条视频和 4 张 Banner 展示。`services/video.ts` 已封装 API 调用，Vite 代理到后端后自动切换为真实数据。

<details>
<summary>接口详情</summary>

### 2.1 GET `/video/loadRecommendVideo`
- **返回**: `Video[]`

### 2.2 GET `/video/loadVideo`
- **请求参数**: `pCategoryId`, `categoryId`, `pageNo`(必填,≥1)
- **返回**: `PaginationResultVO<Video>`

### 2.3 GET `/video/getVideoInfo`
- **请求参数**: `videoId`(必填)
- **返回**: `Video`（自动+1播放量、记录观看历史）

### 2.6 GET `/video/search`
- **请求参数**: `keyword`(必填), `pageNo`(必填,≥1)
- **返回**: `PaginationResultVO<Video>`
- **副作用**: 关键词写入 Redis ZSet 热搜排行

### 2.7 GET `/video/getSearchKeywordTop`
- **返回**: `string[]` — 热搜前10关键词

### 2.8 GET `/video/getVideoRecommend`
- **返回**: `Video[]`

### 2.9 GET `/video/loadHotVideoList`
- **返回**: `Video[]`
</details>

---

## 3. 分类模块 `/category`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | GET/POST | `/category/loadAllCategory` | 加载所有分类（树形结构） | ✅ | ⚠️ |
| 2 | GET/POST | `/category/loadAllParentCategory` | 加载父分类（平铺列表） | ✅ | ❌ |

> ⚠️ 前端 `services/category.ts` 已封装 API，暂无分类页面。

---

## 4. 用户互动模块 `/userAction`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | POST | `/userAction/doAction` | 执行互动操作 | ✅ | ❌ |
| 2 | GET/POST | `/userAction/getUserActionStatus` | 查询互动状态 | ✅ | ❌ |

**操作类型**：`1=点赞` `2=投币` `3=收藏` `4=分享`

---

## 5. 评论模块 `/comment`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | POST | `/comment/postComment` | 发表评论 | ✅ | ❌ |
| 2 | GET/POST | `/comment/loadComment` | 加载评论列表 | ✅ | ❌ |
| 3 | POST | `/comment/likeComment` | 点赞评论 | ✅ | ❌ |
| 4 | GET/POST | `/comment/loadReply` | 加载回复列表 | ✅ | ❌ |
| 5 | POST | `/comment/topComment` | 置顶评论 | ✅ | ❌ |
| 6 | POST | `/comment/cancelTopComment` | 取消置顶 | ✅ | ❌ |
| 7 | POST | `/comment/userDelComment` | 删除自己的评论 | ✅ | ❌ |

---

## 6. 弹幕模块 `/danmu`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | POST | `/danmu/postDanmu` | 发送弹幕 | ✅ | ❌ |
| 2 | GET/POST | `/danmu/loadDanmu` | 加载弹幕列表 | ✅ | ❌ |

---

## 7. 消息模块 `/message`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | GET/POST | `/message/getNoReadCount` | 获取未读消息数 | ✅ | ❌ |
| 2 | GET/POST | `/message/loadMessage` | 分页加载消息 | ✅ | ❌ |
| 3 | POST | `/message/delMessage` | 删除消息 | ✅ | ❌ |
| 4 | GET/POST | `/message/getNoReadCountGroup` | 分组统计未读数 | ✅ | ❌ |
| 5 | POST | `/message/readAll` | 全部标为已读 | ✅ | ❌ |

---

## 8. 个人主页模块 `/uhome`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | POST | `/uhome/updateUserInfo` | 修改个人资料 | ✅ | ❌ |
| 2 | GET/POST | `/uhome/loadVideoList` | 加载用户投稿列表 | ✅ | ❌ |
| 3 | GET/POST | `/uhome/getUserInfo` | 获取用户公开信息 | ✅ | ❌ |
| 4 | POST | `/uhome/focus` | 关注用户 | ✅ | ❌ |
| 5 | POST | `/uhome/cancelFocus` | 取消关注 | ✅ | ❌ |
| 6 | GET/POST | `/uhome/loadFocusList` | 关注列表 | ✅ | ❌ |
| 7 | GET/POST | `/uhome/loadFansList` | 粉丝列表 | ✅ | ❌ |
| 8 | GET/POST | `/uhome/loadUserCollection` | 收藏列表 | ✅ | ❌ |
| 9 | GET/POST | `/uhome/getCoinBalance` | 硬币余额 | ✅ | ❌ |
| 10 | GET/POST | `/uhome/isFocus` | 查询是否关注 | ✅ | ❌ |
| 11 | POST | `/uhome/saveTheme` | 保存用户主题 | ✅ | ❌ |

---

## 9. 创作中心模块 `/ucenter`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | POST | `/ucenter/postVideo` | 发布/编辑视频 | ✅ | ❌ |
| 2 | GET/POST | `/ucenter/loadVideoList` | 我的稿件列表 | ✅ | ❌ |
| 3 | POST | `/ucenter/getVideoCountInfo` | 创作中心统计数据 | ✅ | ❌ |
| 4 | POST | `/ucenter/getVideoByVideoId` | 获取我的视频详情 | ✅ | ❌ |
| 5 | POST | `/ucenter/saveVideoInteraction` | 保存视频互动设置 | ✅ | ❌ |
| 6 | POST | `/ucenter/delVideo` | 删除视频 | ✅ | ❌ |
| 7 | GET/POST | `/ucenter/loadVideoPList` | 获取视频分P列表（我的） | ✅ | ❌ |

---

## 10. 文件模块 `/file`

| # | 方法 | 路径 | 功能 | 后端 | 前端 |
|---|------|------|------|:---:|:---:|
| 1 | POST | `/file/preUploadVideo` | 预上传（获取 uploadId） | ✅ | ❌ |
| 2 | POST | `/file/uploadVideo` | 分片上传视频 | ✅ | ❌ |
| 3 | POST | `/file/delUploadVideo` | 取消上传 | ✅ | ❌ |
| 4 | POST | `/file/uploadImage` | 上传图片（支持缩略图） | ✅ | ❌ |
| 5 | GET | `/file/videoResource/{fileId}` | 获取视频文件（播放） | ✅ | ❌ |
| 6 | GET | `/file/videoOriginal/{fileId}` | 获取原始视频文件 | ✅ | ❌ |
| 7 | GET | `/file/transcodeStatus/{fileId}` | 查询转码状态 | ✅ | ❌ |

---

## 11. 系列 / 历史 / 设置 / 管理后台

| 模块 | 接口数 | 后端 | 前端 |
|------|:-----:|:---:|:---:|
| 系列 `/series` | 1 | ✅ | ❌ |
| 历史 `/history` | 3 | ✅ | ❌ |
| 系统设置 `/sysSetting` | 1 | ✅ | ❌ |
| 管理后台 `/admin/*` | 17 | ✅ | ❌ |

---

## 12. 数据模型速查

### Video（视频）
```
videoId, userId, nickName, avatar, fileId,
videoName, videoCover, categoryId, pCategoryId, postType, tags,
duration, playCount, likeCount, coinCount, collectCount,
commentCount, danmuCount, shareCount, status, postTime
```

### API 通用响应
```json
{ "code": 0, "message": "success", "data": <T> }
```

### 分页响应
```json
{
  "code": 0, "message": "success",
  "data": {
    "pageInfo": { "pageNo", "pageSize", "totalCount", "pageTotal" },
    "list": [...]
  }
}
```

---

## 13. 汇总

| 模块 | 接口数 | 后端已实现 | 前端已接入 API | 前端有页面 |
|------|:-----:|:---------:|:-------------:|:---------:|
| 账户 | 6 | 6 | 0 | 0 |
| 视频 | 9 | 9 | 4 | 1（首页） |
| 分类 | 2 | 2 | 1 | 0 |
| 用户互动 | 2 | 2 | 0 | 0 |
| 评论 | 7 | 7 | 0 | 0 |
| 弹幕 | 2 | 2 | 0 | 0 |
| 消息 | 5 | 5 | 0 | 0 |
| 个人主页 | 11 | 11 | 0 | 0 |
| 创作中心 | 7 | 7 | 0 | 0 |
| 文件 | 7 | 7 | 0 | 0 |
| 系列 | 1 | 1 | 0 | 0 |
| 历史 | 3 | 3 | 0 | 0 |
| 系统设置 | 1 | 1 | 0 | 0 |
| 管理后台 | 17 | 17 | 0 | 0 |
| **总计** | **80** | **80** | **5** | **1** |
