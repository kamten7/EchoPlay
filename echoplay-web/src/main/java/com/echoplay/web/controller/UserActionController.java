package com.echoplay.web.controller;

import com.echoplay.component.RedisComponent;
import com.echoplay.entity.dto.TokenUserInfoDto;
import com.echoplay.entity.enums.ResponseCodeEnum;
import com.echoplay.entity.vo.ResponseVO;
import com.echoplay.exception.BusinessException;
import com.echoplay.entity.po.UserAction;
import com.echoplay.service.UserActionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@RestController("webUserActionController")
@RequestMapping("/userAction")
@Validated
public class UserActionController extends ABaseController {

    @Resource
    private UserActionService userActionService;

    @Resource
    private RedisComponent redisComponent;

    @RequestMapping("/doAction")
    public ResponseVO doAction(@NotEmpty String videoId, @NotNull Integer actionType,
                               Integer actionCount, String commentId) {
        String token = getTokenFromCookie();
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }
        userActionService.doAction(tokenUserInfoDto, videoId, actionType, actionCount, commentId);
        return getSuccessResponseVO(null);
    }

    @RequestMapping("/getUserActionStatus")
    public ResponseVO getUserActionStatus(@NotEmpty String videoId, @NotNull Integer actionType) {
        String token = getTokenFromCookie();
        TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfo(token);
        if (tokenUserInfoDto == null) {
            throw new BusinessException(ResponseCodeEnum.UNAUTHORIZED);
        }
        UserAction action = userActionService.getUserAction(
                tokenUserInfoDto.getUserId(), videoId, actionType);
        return getSuccessResponseVO(action != null && action.getIsDeleted() == 0);
    }
}
