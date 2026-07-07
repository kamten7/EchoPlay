package com.echoplay.admin.controller;

import com.echoplay.entity.vo.ResponseVO;
import com.echoplay.service.VideoFileService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;

@RestController("adminFileController")
@RequestMapping("/file")
@Validated
public class FileController extends ABaseController {

    @Autowired
    private VideoFileService videoFileService;

    @RequestMapping("/uploadImage")
    public ResponseVO uploadImage(MultipartFile file, Boolean createThumbnail) throws Exception {
        if (file == null || file.isEmpty()) {
            return getSuccessResponseVO(null);
        }
        File tempFile = File.createTempFile("img_", file.getOriginalFilename());
        file.transferTo(tempFile);
        String ossUrl = videoFileService.uploadImage(tempFile.getAbsolutePath(), createThumbnail);
        return getSuccessResponseVO(ossUrl);
    }
}
