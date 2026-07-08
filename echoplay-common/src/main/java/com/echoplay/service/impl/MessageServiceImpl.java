package com.echoplay.service.impl;

import com.echoplay.entity.constants.Constants;
import com.echoplay.entity.po.Message;
import com.echoplay.entity.po.UserInfo;
import com.echoplay.entity.query.MessageQuery;
import com.echoplay.entity.query.SimplePage;
import com.echoplay.entity.vo.PaginationResultVO;
import com.echoplay.exception.BusinessException;
import com.echoplay.mappers.MessageMapper;
import com.echoplay.mappers.UserInfoMapper;
import com.echoplay.mappers.VideoMapper;
import com.echoplay.service.MessageService;
import com.echoplay.utils.StringTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Long getNoReadCount(String receiveUserId) {
        return messageMapper.selectNoReadCount(receiveUserId);
    }

    @Override
    public PaginationResultVO<Message> loadMessage(String receiveUserId, Integer pageNo) {
        MessageQuery query = new MessageQuery();
        query.setReceiveUserId(receiveUserId);
        query.setPageNo(pageNo);
        query.setOrderBy("create_time");
        query.setOrderDirection("desc");

        Long count = messageMapper.selectCountByCondition(query);
        SimplePage page = new SimplePage(pageNo, query.getPageSize(), count);
        List<Message> list = messageMapper.selectListByCondition(query);

        // Enrich with sender info
        for (Message msg : list) {
            if (msg.getSendUserId() != null) {
                UserInfo sender = userInfoMapper.selectByUserId(msg.getSendUserId());
                if (sender != null) {
                    msg.setSendNickName(sender.getNickName());
                    msg.setSendAvatar(sender.getAvatar());
                }
            }
        }

        return new PaginationResultVO<>(page, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMessage(String messageId, String userId) {
        Message message = messageMapper.selectByMessageId(messageId);
        if (message == null || !message.getReceiveUserId().equals(userId)) {
            throw new BusinessException("无权删除该消息");
        }
        messageMapper.deleteByMessageId(messageId);
    }

    @Override
    public List<Map<String, Object>> getNoReadCountGroup(String receiveUserId) {
        MessageQuery query = new MessageQuery();
        query.setReceiveUserId(receiveUserId);
        query.setIsRead(0);
        query.setPageNo(null);
        query.setPageSize(null);
        List<Message> messageList = messageMapper.selectListByCondition(query);

        Map<Integer, Long> groupMap = new HashMap<>();
        for (Message msg : messageList) {
            Integer type = msg.getMessageType();
            groupMap.put(type, groupMap.getOrDefault(type, 0L) + 1);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : groupMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("messageType", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readAll(String receiveUserId) {
        messageMapper.readAll(receiveUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMessage(String receiveUserId, String sendUserId, Integer messageType, String content, String relatedId) {
        Message message = new Message();
        message.setMessageId(StringTools.getRandomNumber(Constants.LENGTH_10));
        message.setReceiveUserId(receiveUserId);
        message.setSendUserId(sendUserId);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setRelatedId(relatedId);
        message.setIsRead(0);
        message.setCreateTime(new Date());
        message.setUpdateTime(new Date());
        messageMapper.insert(message);
    }
}