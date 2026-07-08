package com.echoplay.service;

import com.echoplay.entity.dto.TokenUserInfoDto;
import com.echoplay.entity.po.UserAction;

public interface UserActionService {
    void doAction(TokenUserInfoDto token, String videoId, Integer actionType, Integer actionCount, String commentId);
    UserAction getUserAction(String userId, String videoId, Integer actionType);
}
