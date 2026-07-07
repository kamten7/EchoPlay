package com.myvideoplatform.web.controller;

import com.myvideoplatform.component.RedisComponent;
import com.myvideoplatform.entity.dto.TokenUserInfoDto;
import com.myvideoplatform.entity.enums.ResponseCodeEnum;
import com.myvideoplatform.entity.vo.PaginationResultVO;
import com.myvideoplatform.entity.vo.ResponseVO;
import com.myvideoplatform.exception.BusinessException;
import com.myvideoplatform.service.UserWatchHistoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController("webHistoryController")
@RequestMapping("/history")
@Validated
public class HistoryController extends ABaseController {

    @Resource
    private UserWatchHistoryService userWatchHistoryService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/loadHistory")
    public ResponseVO loadHistory(@RequestParam(required = false, defaultValue = "1") Integer pageNo) {
        String token = getTokenFromCookie();
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }
        PaginationResultVO<Map<String, Object>> result = userWatchHistoryService.loadHistory(
                tokenUserInfoDto.getUserId(), pageNo);
        return getSuccessResponseVO(result);
    }

    @RequestMapping("/delHistory")
    public ResponseVO delHistory(String videoId) {
        String token = getTokenFromCookie();
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }
        userWatchHistoryService.delHistory(tokenUserInfoDto.getUserId(), videoId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/cleanHistory")
    public ResponseVO cleanHistory() {
        String token = getTokenFromCookie();
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }
        userWatchHistoryService.cleanHistory(tokenUserInfoDto.getUserId());
        return getSuccessResponseVO(null);
    }
}
