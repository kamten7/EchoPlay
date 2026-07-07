package com.myvideoplatform.service;

import com.myvideoplatform.entity.dto.TokenUserInfoDto;
import com.myvideoplatform.entity.po.VideoSeries;
import com.myvideoplatform.entity.vo.PaginationResultVO;

import java.util.List;
import java.util.Map;

public interface VideoSeriesService {
    List<VideoSeries> loadVideoSeries(String userId);
    void saveVideoSeries(TokenUserInfoDto token, String seriesId, String seriesName, String seriesDescription);
    void delVideoSeries(String seriesId, String userId);
    void changeVideoSeriesSort(String seriesId, Integer sort, String userId);
    List<Map<String, Object>> loadAllVideo(String seriesId, String userId);
    void saveSeriesVideo(String seriesId, String videoId, String userId);
    void delSeriesVideo(String seriesId, String videoId, String userId);
    VideoSeries getVideoSeriesDetail(String seriesId);
    List<Map<String, Object>> loadVideoSeriesWithVideo(String userId);
}
