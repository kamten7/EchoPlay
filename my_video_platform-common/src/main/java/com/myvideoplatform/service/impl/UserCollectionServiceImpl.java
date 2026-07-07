package com.myvideoplatform.service.impl;

import com.myvideoplatform.entity.enums.UserActionTypeEnum;
import com.myvideoplatform.entity.po.UserAction;
import com.myvideoplatform.entity.po.Video;
import com.myvideoplatform.entity.query.SimplePage;
import com.myvideoplatform.entity.query.UserActionQuery;
import com.myvideoplatform.entity.vo.PaginationResultVO;
import com.myvideoplatform.mappers.UserActionMapper;
import com.myvideoplatform.mappers.VideoMapper;
import com.myvideoplatform.service.UserCollectionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service("userCollectionService")
public class UserCollectionServiceImpl implements UserCollectionService {

    @Resource
    private UserActionMapper userActionMapper;
    @Resource
    private VideoMapper videoMapper;

    @Override
    public PaginationResultVO<Map<String, Object>> loadUserCollection(String userId, Integer pageNo) {
        UserActionQuery query = new UserActionQuery();
        query.setUserId(userId);
        query.setActionType(UserActionTypeEnum.COLLECT.getCode());
        query.setPageNo(pageNo);

        Long count = userActionMapper.selectCountByCondition(query);
        SimplePage page = new SimplePage(pageNo, query.getPageSize(), count);
        List<UserAction> actionList = userActionMapper.selectListByCondition(query);

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (UserAction action : actionList) {
            Map<String, Object> map = new HashMap<>();
            map.put("userAction", action);
            Video video = videoMapper.selectByVideoId(action.getVideoId());
            if (video != null) {
                map.put("video", video);
            }
            resultList.add(map);
        }

        return new PaginationResultVO<>(page, resultList);
    }
}
