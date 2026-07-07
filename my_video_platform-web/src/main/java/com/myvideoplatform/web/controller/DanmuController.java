package com.myvideoplatform.web.controller;

import com.myvideoplatform.component.RedisComponent;
import com.myvideoplatform.entity.dto.TokenUserInfoDto;
import com.myvideoplatform.entity.enums.ResponseCodeEnum;
import com.myvideoplatform.entity.po.Danmu;
import com.myvideoplatform.entity.vo.ResponseVO;
import com.myvideoplatform.exception.BusinessException;
import com.myvideoplatform.service.DanmuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController("webDanmuController")
@RequestMapping("/danmu")
@Validated
public class DanmuController extends ABaseController {

    @Resource
    private DanmuService danmuService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/postDanmu")
    public ResponseVO postDanmu(@NotEmpty String videoId, @NotEmpty String fileId,
                                @NotEmpty String text, @NotNull Integer mode,
                                String color, @NotNull Integer time) {
        String token = getTokenFromCookie();
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }
        danmuService.postDanmu(tokenUserInfoDto, videoId, fileId, text, mode, color, time);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/loadDanmu")
    public ResponseVO loadDanmu(@NotEmpty String fileId, @NotEmpty String videoId) {
        List<Danmu> result = danmuService.loadDanmu(fileId, videoId);
        return getSuccessResponseVO(result);
    }
}
