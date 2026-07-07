package com.myvideoplatform.service;

import com.myvideoplatform.entity.dto.TokenUserInfoDto;
import com.myvideoplatform.entity.po.Video;
import com.myvideoplatform.entity.query.VideoQuery;
import com.myvideoplatform.entity.vo.PaginationResultVO;

import java.util.List;

public interface VideoService {
    PaginationResultVO<Video> loadVideoPage(VideoQuery query);
    Video getVideoByVideoId(String videoId);
    List<Video> loadRecommendVideo();
    List<Video> loadHotVideoList();
    List<Video> getVideoRecommend();
    String postVideo(TokenUserInfoDto token, String videoId, String videoCover, String videoName, Integer pCategoryId, Integer categoryId, Integer postType, String tags, String introduction, String interaction, String uploadFileList);
    PaginationResultVO<Video> loadVideoListByUserId(String userId, Integer status, String videoNameFuzzy, Integer pageNo);
    Video getVideoByVideoIdForUser(String videoId, String userId);
    void saveVideoInteraction(String videoId, String interaction, String userId);
    void deleteVideo(String videoId, String userId);
}
