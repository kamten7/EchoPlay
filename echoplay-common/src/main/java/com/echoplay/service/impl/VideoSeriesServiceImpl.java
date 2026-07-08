package com.echoplay.service.impl;

import com.echoplay.entity.constants.Constants;
import com.echoplay.entity.dto.TokenUserInfoDto;
import com.echoplay.entity.enums.ResponseCodeEnum;
import com.echoplay.entity.po.SeriesVideo;
import com.echoplay.entity.po.Video;
import com.echoplay.entity.po.VideoSeries;
import com.echoplay.entity.query.SeriesVideoQuery;
import com.echoplay.entity.query.VideoSeriesQuery;
import com.echoplay.exception.BusinessException;
import com.echoplay.mappers.SeriesVideoMapper;
import com.echoplay.mappers.VideoMapper;
import com.echoplay.mappers.VideoSeriesMapper;
import com.echoplay.service.VideoSeriesService;
import com.echoplay.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("videoSeriesService")
public class VideoSeriesServiceImpl implements VideoSeriesService {

    @Resource
    private VideoSeriesMapper videoSeriesMapper;
    @Resource
    private SeriesVideoMapper seriesVideoMapper;
    @Resource
    private VideoMapper videoMapper;

    @Override
    public List<VideoSeries> loadVideoSeries(String userId) {
        VideoSeriesQuery query = new VideoSeriesQuery();
        query.setUserId(userId);
        query.setPageNo(null);
        query.setPageSize(null);
        query.setOrderBy("sort");
        query.setOrderDirection("asc");
        return videoSeriesMapper.selectListByCondition(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveVideoSeries(TokenUserInfoDto token, String seriesId, String seriesName, String seriesDescription) {
        if (StringTools.isEmpty(seriesId)) {
            VideoSeries series = new VideoSeries();
            series.setSeriesId(StringTools.getRandomNumber(Constants.LENGTH_10));
            series.setUserId(token.getUserId());
            series.setSeriesName(seriesName);
            series.setSeriesDescription(seriesDescription);
            series.setSort(0);
            series.setCreateTime(new Date());
            series.setUpdateTime(new Date());
            videoSeriesMapper.insert(series);
        } else {
            VideoSeries dbSeries = videoSeriesMapper.selectBySeriesId(seriesId);
            if (dbSeries == null || !dbSeries.getUserId().equals(token.getUserId())) {
                throw new BusinessException(ResponseCodeEnum.BUSINESS_ERROR);
            }
            dbSeries.setSeriesName(seriesName);
            dbSeries.setSeriesDescription(seriesDescription);
            dbSeries.setUpdateTime(new Date());
            videoSeriesMapper.updateBySeriesId(dbSeries);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delVideoSeries(String seriesId, String userId) {
        VideoSeries series = videoSeriesMapper.selectBySeriesId(seriesId);
        if (series == null || !series.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.BUSINESS_ERROR);
        }
        videoSeriesMapper.deleteBySeriesId(seriesId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeVideoSeriesSort(String seriesId, Integer sort, String userId) {
        VideoSeries series = videoSeriesMapper.selectBySeriesId(seriesId);
        if (series == null || !series.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.BUSINESS_ERROR);
        }
        series.setSort(sort);
        series.setUpdateTime(new Date());
        videoSeriesMapper.updateBySeriesId(series);
    }

    @Override
    public List<Map<String, Object>> loadAllVideo(String seriesId, String userId) {
        VideoSeries series = videoSeriesMapper.selectBySeriesId(seriesId);
        if (series == null || !series.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.BUSINESS_ERROR);
        }

        SeriesVideoQuery svQuery = new SeriesVideoQuery();
        svQuery.setSeriesId(seriesId);
        svQuery.setPageNo(null);
        svQuery.setPageSize(null);
        List<SeriesVideo> svList = seriesVideoMapper.selectListByCondition(svQuery);

        List<Map<String, Object>> result = new ArrayList<>();
        for (SeriesVideo sv : svList) {
            Map<String, Object> map = new HashMap<>();
            map.put("seriesVideo", sv);
            Video video = videoMapper.selectByVideoId(sv.getVideoId());
            map.put("video", video);
            result.add(map);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSeriesVideo(String seriesId, String videoId, String userId) {
        VideoSeries series = videoSeriesMapper.selectBySeriesId(seriesId);
        if (series == null || !series.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.BUSINESS_ERROR);
        }
        SeriesVideo seriesVideo = new SeriesVideo();
        seriesVideo.setSeriesId(seriesId);
        seriesVideo.setVideoId(videoId);
        seriesVideo.setCreateTime(new Date());
        seriesVideo.setUpdateTime(new Date());
        seriesVideoMapper.insert(seriesVideo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delSeriesVideo(String seriesId, String videoId, String userId) {
        VideoSeries series = videoSeriesMapper.selectBySeriesId(seriesId);
        if (series == null || !series.getUserId().equals(userId)) {
            throw new BusinessException(ResponseCodeEnum.BUSINESS_ERROR);
        }
        seriesVideoMapper.deleteBySeriesVideo(seriesId, videoId);
    }

    @Override
    public VideoSeries getVideoSeriesDetail(String seriesId) {
        return videoSeriesMapper.selectBySeriesId(seriesId);
    }

    @Override
    public List<Map<String, Object>> loadVideoSeriesWithVideo(String userId) {
        List<VideoSeries> seriesList = loadVideoSeries(userId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (VideoSeries series : seriesList) {
            Map<String, Object> map = new HashMap<>();
            map.put("series", series);

            SeriesVideoQuery svQuery = new SeriesVideoQuery();
            svQuery.setSeriesId(series.getSeriesId());
            svQuery.setPageNo(null);
            svQuery.setPageSize(null);
            List<SeriesVideo> svList = seriesVideoMapper.selectListByCondition(svQuery);

            List<Video> videoList = new ArrayList<>();
            for (SeriesVideo sv : svList) {
                Video video = videoMapper.selectByVideoId(sv.getVideoId());
                if (video != null) {
                    videoList.add(video);
                }
            }
            map.put("videos", videoList);
            result.add(map);
        }
        return result;
    }
}
