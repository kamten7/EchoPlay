package com.myvideoplatform.service;

import com.myvideoplatform.entity.po.VideoFile;
import com.myvideoplatform.entity.query.VideoFileQuery;

import java.util.List;
import java.util.Map;

public interface VideoFileService {
    Map<String, Object> preUploadVideo(String fileName, Integer chunks, String userId);
    Map<String, Object> uploadVideo(String chunkFile, Integer chunkIndex, String uploadId);
    void delUploadVideo(String uploadId);
    String uploadImage(String file, Boolean createThumbnail);
    VideoFile getVideoFileByFileId(String fileId);
    VideoFile getVideoFileByUploadId(String uploadId);
    List<VideoFile> getVideoFileList(VideoFileQuery query);
    void saveVideoFile(VideoFile videoFile);
}
