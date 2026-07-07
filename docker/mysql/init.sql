-- ============================================
-- Kilikili 数据库初始化脚本
-- 数据库: kilikili
-- ============================================

CREATE DATABASE IF NOT EXISTS `kilikili` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `kilikili`;

-- ----------------------------
-- 用户信息表
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `userId` varchar(64) NOT NULL COMMENT '用户ID',
  `nickName` varchar(64) DEFAULT '' COMMENT '昵称',
  `email` varchar(128) DEFAULT '' COMMENT '邮箱',
  `password` varchar(128) DEFAULT '' COMMENT '密码',
  `avatar` varchar(512) DEFAULT '' COMMENT '头像URL',
  `sex` int(1) DEFAULT '0' COMMENT '性别 0-未知 1-男 2-女',
  `birthday` varchar(16) DEFAULT '' COMMENT '生日',
  `school` varchar(128) DEFAULT '' COMMENT '学校',
  `personIntroduction` varchar(512) DEFAULT '' COMMENT '个人简介',
  `joinTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  `lastLoginTime` datetime DEFAULT NULL COMMENT '最后登录时间',
  `lastLoginIp` varchar(64) DEFAULT '' COMMENT '最后登录IP',
  `status` int(1) DEFAULT '1' COMMENT '状态 0-禁用 1-正常',
  `noticeInfo` varchar(1024) DEFAULT '' COMMENT '空间公告',
  `coinCount` int(11) DEFAULT '0' COMMENT '硬币总数',
  `currentCoinCount` int(11) DEFAULT '0' COMMENT '当前硬币数',
  `theme` int(1) DEFAULT '0' COMMENT '主题 0-亮色 1-暗色',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- ----------------------------
-- 视频信息表
-- ----------------------------
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `videoId` varchar(64) NOT NULL COMMENT '视频ID',
  `userId` varchar(64) NOT NULL COMMENT '上传者用户ID',
  `videoName` varchar(256) DEFAULT '' COMMENT '视频名称',
  `videoCover` varchar(512) DEFAULT '' COMMENT '视频封面',
  `categoryId` int(11) DEFAULT '0' COMMENT '一级分类ID',
  `pCategoryId` int(11) DEFAULT '0' COMMENT '二级分类ID',
  `postType` int(1) DEFAULT '1' COMMENT '投稿类型 1-原创 2-转载',
  `tags` varchar(512) DEFAULT '' COMMENT '标签(逗号分隔)',
  `introduction` varchar(2048) DEFAULT '' COMMENT '简介',
  `interaction` varchar(1024) DEFAULT '' COMMENT '互动语',
  `duration` int(11) DEFAULT '0' COMMENT '时长(秒)',
  `playCount` int(11) DEFAULT '0' COMMENT '播放量',
  `likeCount` int(11) DEFAULT '0' COMMENT '点赞数',
  `coinCount` int(11) DEFAULT '0' COMMENT '投币数',
  `collectCount` int(11) DEFAULT '0' COMMENT '收藏数',
  `commentCount` int(11) DEFAULT '0' COMMENT '评论数',
  `danmuCount` int(11) DEFAULT '0' COMMENT '弹幕数',
  `shareCount` int(11) DEFAULT '0' COMMENT '分享数',
  `status` int(1) DEFAULT '0' COMMENT '状态 0-待审核 1-已通过 2-已拒绝',
  `recommendType` int(1) DEFAULT '0' COMMENT '推荐类型 0-不推荐 1-首页推荐',
  `auditReason` varchar(512) DEFAULT '' COMMENT '拒绝原因',
  `lastPlayTime` datetime DEFAULT NULL COMMENT '最后播放时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDeleted` int(1) DEFAULT '0' COMMENT '逻辑删除 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_videoId` (`videoId`),
  KEY `idx_userId` (`userId`),
  KEY `idx_categoryId` (`categoryId`),
  KEY `idx_status` (`status`),
  KEY `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频信息表';

-- ----------------------------
-- 视频分P表
-- ----------------------------
DROP TABLE IF EXISTS `video_p`;
CREATE TABLE `video_p` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pId` varchar(64) NOT NULL COMMENT '分P ID',
  `videoId` varchar(64) NOT NULL COMMENT '所属视频ID',
  `pName` varchar(256) DEFAULT '' COMMENT '分P名称',
  `fileId` varchar(64) DEFAULT '' COMMENT '文件ID',
  `duration` int(11) DEFAULT '0' COMMENT '时长(秒)',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pId` (`pId`),
  KEY `idx_videoId` (`videoId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频分P表';

-- ----------------------------
-- 视频文件表
-- ----------------------------
DROP TABLE IF EXISTS `video_file`;
CREATE TABLE `video_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fileId` varchar(64) NOT NULL COMMENT '文件ID',
  `userId` varchar(64) NOT NULL COMMENT '上传者用户ID',
  `fileName` varchar(256) DEFAULT '' COMMENT '文件名',
  `fileSize` bigint(20) DEFAULT '0' COMMENT '文件大小(字节)',
  `filePath` varchar(1024) DEFAULT '' COMMENT '存储路径',
  `fileSuffix` varchar(16) DEFAULT '' COMMENT '文件后缀',
  `md5` varchar(32) DEFAULT '' COMMENT 'MD5校验值',
  `chunkCount` int(11) DEFAULT '0' COMMENT '分片总数',
  `chunkSize` bigint(20) DEFAULT '0' COMMENT '分片大小(字节)',
  `uploadId` varchar(128) DEFAULT '' COMMENT '上传任务ID',
  `status` int(1) DEFAULT '0' COMMENT '状态 0-上传中 1-转码中 2-转码完成 3-转码失败',
  `duration` int(11) DEFAULT '0' COMMENT '时长(秒)',
  `videoCover` varchar(512) DEFAULT '' COMMENT '视频封面',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_fileId` (`fileId`),
  KEY `idx_userId` (`userId`),
  KEY `idx_uploadId` (`uploadId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频文件表';

-- ----------------------------
-- 上传记录表
-- ----------------------------
DROP TABLE IF EXISTS `upload_record`;
CREATE TABLE `upload_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uploadId` varchar(128) NOT NULL COMMENT '上传任务ID',
  `userId` varchar(64) NOT NULL COMMENT '用户ID',
  `fileName` varchar(256) DEFAULT '' COMMENT '文件名',
  `fileSize` bigint(20) DEFAULT '0' COMMENT '文件大小',
  `chunkCount` int(11) DEFAULT '0' COMMENT '总分片数',
  `uploadedChunks` int(11) DEFAULT '0' COMMENT '已上传分片数',
  `md5` varchar(32) DEFAULT '' COMMENT '文件MD5',
  `filePath` varchar(1024) DEFAULT '' COMMENT '临时存储路径',
  `status` int(1) DEFAULT '0' COMMENT '状态 0-上传中 1-已完成 2-已取消',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_uploadId` (`uploadId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='上传记录表';

-- ----------------------------
-- 分类表
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `categoryId` int(11) NOT NULL COMMENT '分类ID',
  `pCategoryId` int(11) DEFAULT '0' COMMENT '父分类ID 0表示一级分类',
  `categoryCode` varchar(32) DEFAULT '' COMMENT '分类编码',
  `categoryName` varchar(64) DEFAULT '' COMMENT '分类名称',
  `icon` varchar(256) DEFAULT '' COMMENT '图标',
  `background` varchar(256) DEFAULT '' COMMENT '背景图',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_categoryId` (`categoryId`),
  KEY `idx_pCategoryId` (`pCategoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- ----------------------------
-- 评论表
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `commentId` varchar(64) NOT NULL COMMENT '评论ID',
  `videoId` varchar(64) NOT NULL COMMENT '视频ID',
  `userId` varchar(64) NOT NULL COMMENT '评论者用户ID',
  `content` varchar(1024) DEFAULT '' COMMENT '评论内容',
  `imgPath` varchar(512) DEFAULT '' COMMENT '图片路径',
  `pCommentId` varchar(64) DEFAULT '' COMMENT '父评论ID',
  `replyUserId` varchar(64) DEFAULT '' COMMENT '被回复用户ID',
  `likeCount` int(11) DEFAULT '0' COMMENT '点赞数',
  `topType` int(1) DEFAULT '0' COMMENT '置顶类型 0-不置顶 1-置顶',
  `status` int(1) DEFAULT '1' COMMENT '状态 0-待审核 1-通过 2-拒绝',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_commentId` (`commentId`),
  KEY `idx_videoId` (`videoId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ----------------------------
-- 弹幕表
-- ----------------------------
DROP TABLE IF EXISTS `danmu`;
CREATE TABLE `danmu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `danmuId` varchar(64) NOT NULL COMMENT '弹幕ID',
  `videoId` varchar(64) NOT NULL COMMENT '视频ID',
  `fileId` varchar(64) DEFAULT '' COMMENT '文件ID',
  `userId` varchar(64) NOT NULL COMMENT '用户ID',
  `content` varchar(512) DEFAULT '' COMMENT '弹幕内容',
  `timePoint` int(11) DEFAULT '0' COMMENT '时间点(秒)',
  `mode` int(1) DEFAULT '0' COMMENT '模式 0-滚动 1-顶部 2-底部',
  `color` varchar(16) DEFAULT '#ffffff' COMMENT '颜色',
  `fontSize` int(11) DEFAULT '25' COMMENT '字号',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_danmuId` (`danmuId`),
  KEY `idx_videoId` (`videoId`),
  KEY `idx_fileId` (`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='弹幕表';

-- ----------------------------
-- 用户行为表（点赞/收藏/投币/分享）
-- ----------------------------
DROP TABLE IF EXISTS `user_action`;
CREATE TABLE `user_action` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` varchar(64) NOT NULL COMMENT '用户ID',
  `videoId` varchar(64) DEFAULT '' COMMENT '视频ID',
  `commentId` varchar(64) DEFAULT '' COMMENT '评论ID',
  `actionType` int(11) NOT NULL COMMENT '行为类型 1-点赞 2-投币 3-收藏 4-分享',
  `actionCount` int(11) DEFAULT '1' COMMENT '行为次数(投币次数)',
  `lastActionTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后操作时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_video_action` (`userId`,`videoId`,`actionType`),
  KEY `idx_videoId` (`videoId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为表';

-- ----------------------------
-- 用户关注表
-- ----------------------------
DROP TABLE IF EXISTS `user_focus`;
CREATE TABLE `user_focus` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` varchar(64) NOT NULL COMMENT '关注者用户ID',
  `focusUserId` varchar(64) NOT NULL COMMENT '被关注者用户ID',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_focus` (`userId`,`focusUserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注表';

-- ----------------------------
-- 消息表
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `messageId` varchar(64) NOT NULL COMMENT '消息ID',
  `receiveUserId` varchar(64) NOT NULL COMMENT '接收者用户ID',
  `sendUserId` varchar(64) DEFAULT '' COMMENT '发送者用户ID',
  `messageType` int(11) NOT NULL COMMENT '消息类型 1-评论回复 2-点赞 3-关注 4-系统通知',
  `content` varchar(1024) DEFAULT '' COMMENT '消息内容',
  `relatedId` varchar(64) DEFAULT '' COMMENT '关联ID(视频ID/评论ID等)',
  `isRead` int(1) DEFAULT '0' COMMENT '是否已读 0-未读 1-已读',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_messageId` (`messageId`),
  KEY `idx_receiveUserId` (`receiveUserId`),
  KEY `idx_isRead` (`isRead`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- ----------------------------
-- 播放历史表
-- ----------------------------
DROP TABLE IF EXISTS `play_history`;
CREATE TABLE `play_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` varchar(64) NOT NULL COMMENT '用户ID',
  `videoId` varchar(64) NOT NULL COMMENT '视频ID',
  `fileId` varchar(64) DEFAULT '' COMMENT '文件ID',
  `progress` int(11) DEFAULT '0' COMMENT '播放进度(秒)',
  `duration` int(11) DEFAULT '0' COMMENT '视频总时长(秒)',
  `lastPlayTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '最后播放时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_video` (`userId`,`videoId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='播放历史表';

-- ----------------------------
-- 观看历史表（无自增ID）
-- ----------------------------
DROP TABLE IF EXISTS `user_watch_history`;
CREATE TABLE `user_watch_history` (
  `userId` varchar(64) NOT NULL COMMENT '用户ID',
  `videoId` varchar(64) NOT NULL COMMENT '视频ID',
  `watchedAt` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '观看时间',
  PRIMARY KEY (`userId`,`videoId`),
  KEY `idx_videoId` (`videoId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='观看历史表';

-- ----------------------------
-- 视频系列表
-- ----------------------------
DROP TABLE IF EXISTS `video_series`;
CREATE TABLE `video_series` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seriesId` varchar(64) NOT NULL COMMENT '系列ID',
  `userId` varchar(64) NOT NULL COMMENT '创建者用户ID',
  `seriesName` varchar(256) DEFAULT '' COMMENT '系列名称',
  `seriesCover` varchar(512) DEFAULT '' COMMENT '系列封面',
  `seriesDescription` varchar(1024) DEFAULT '' COMMENT '系列描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_seriesId` (`seriesId`),
  KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频系列表';

-- ----------------------------
-- 系列视频关联表
-- ----------------------------
DROP TABLE IF EXISTS `series_video`;
CREATE TABLE `series_video` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seriesId` varchar(64) NOT NULL COMMENT '系列ID',
  `videoId` varchar(64) NOT NULL COMMENT '视频ID',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_series_video` (`seriesId`,`videoId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系列视频关联表';

-- ----------------------------
-- 系统设置表
-- ----------------------------
DROP TABLE IF EXISTS `sys_setting`;
CREATE TABLE `sys_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `settingKey` varchar(128) NOT NULL COMMENT '配置键',
  `settingValue` varchar(2048) DEFAULT '' COMMENT '配置值',
  `settingDescription` varchar(512) DEFAULT '' COMMENT '配置描述',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isDeleted` int(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_settingKey` (`settingKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统设置表';

-- ----------------------------
-- 操作日志表
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operId` varchar(64) DEFAULT '' COMMENT '操作ID',
  `operModule` varchar(64) DEFAULT '' COMMENT '操作模块',
  `operType` varchar(64) DEFAULT '' COMMENT '操作类型',
  `operDesc` varchar(1024) DEFAULT '' COMMENT '操作描述',
  `requestUrl` varchar(512) DEFAULT '' COMMENT '请求URL',
  `requestParams` text COMMENT '请求参数',
  `operUserId` varchar(64) DEFAULT '' COMMENT '操作人ID',
  `operUserName` varchar(64) DEFAULT '' COMMENT '操作人姓名',
  `operIp` varchar(64) DEFAULT '' COMMENT '操作IP',
  `status` int(1) DEFAULT '1' COMMENT '状态 0-失败 1-成功',
  `errorMsg` varchar(2048) DEFAULT '' COMMENT '错误信息',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_operUserId` (`operUserId`),
  KEY `idx_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- ----------------------------
-- 插入默认管理员账号
-- ----------------------------
INSERT INTO `user_info` (`userId`, `nickName`, `email`, `password`, `sex`, `status`, `coinCount`, `currentCoinCount`)
VALUES ('admin', '管理员', 'admin@kilikili.com', 'admin123', 0, 1, 100, 100);
