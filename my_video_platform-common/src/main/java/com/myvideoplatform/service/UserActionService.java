package com.myvideoplatform.service;

import com.myvideoplatform.entity.dto.TokenUserInfoDto;
import com.myvideoplatform.entity.po.UserAction;

public interface UserActionService {
    void doAction(TokenUserInfoDto token, String videoId, Integer actionType, Integer actionCount, String commentId);
    UserAction getUserAction(String userId, String videoId, Integer actionType);
}
