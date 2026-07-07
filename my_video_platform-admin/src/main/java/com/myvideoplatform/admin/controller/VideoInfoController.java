package com.myvideoplatform.admin.controller;

import com.myvideoplatform.entity.po.Video;
import com.myvideoplatform.entity.query.VideoPQuery;
import com.myvideoplatform.entity.query.VideoQuery;
import com.myvideoplatform.entity.vo.ResponseVO;
import com.myvideoplatform.mappers.VideoMapper;
import com.myvideoplatform.mappers.VideoPMapper;
import com.myvideoplatform.service.VideoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController("adminVideoInfoController")
@RequestMapping("/videoInfo")
@Validated
public class VideoInfoController extends ABaseController {

    @Resource
    private VideoService videoService;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private VideoPMapper videoPMapper;

    @RequestMapping("/loadVideoList")
    public ResponseVO loadVideoList(Integer pageNo,
                                    Integer pageSize,
                                    String videoNameFuzzy,
                                    Integer status,
                                    Integer categoryId) {
        VideoQuery query = new VideoQuery();
        query.setVideoNameFuzzy(videoNameFuzzy);
        query.setStatus(status);
        query.setCategoryId(categoryId);
        query.setPageNo(pageNo != null ? pageNo : 1);
        query.setPageSize(pageSize != null ? pageSize : 20);
        return getSuccessResponseVO(videoService.loadVideoPage(query));
    }

    @RequestMapping("/auditVideo")
    public ResponseVO auditVideo(@NotEmpty String videoId,
                                 @NotNull Integer status,
                                 String reason) {
        Video video = videoMapper.selectByVideoId(videoId);
        if (video != null) {
            video.setStatus(status);
            video.setAuditReason(reason);
            videoMapper.updateByVideoId(video);
        }
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/deleteVideo")
    public ResponseVO deleteVideo(@NotEmpty String videoId) {
        videoMapper.deleteByVideoId(videoId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/recommendVideo")
    public ResponseVO recommendVideo(@NotEmpty String videoId) {
        Video video = videoMapper.selectByVideoId(videoId);
        if (video != null) {
            video.setRecommendType(video.getRecommendType() == 1 ? 0 : 1);
            videoMapper.updateByVideoId(video);
        }
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadVideoPList")
    public ResponseVO loadVideoPList(@NotEmpty String videoId) {
        VideoPQuery query = new VideoPQuery();
        query.setVideoId(videoId);
        return getSuccessResponseVO(videoPMapper.selectListByCondition(query));
    }
}
