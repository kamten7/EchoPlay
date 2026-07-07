package com.myvideoplatform.service.impl;

import com.myvideoplatform.component.RedisComponent;
import com.myvideoplatform.entity.constants.Constants;
import com.myvideoplatform.entity.dto.TokenUserInfoDto;
import com.myvideoplatform.entity.enums.ResponseCodeEnum;
import com.myvideoplatform.entity.enums.SexEnum;
import com.myvideoplatform.entity.enums.UserStatusEnum;
import com.myvideoplatform.entity.po.UserInfo;
import com.myvideoplatform.entity.query.SimplePage;
import com.myvideoplatform.entity.query.UserInfoQuery;
import com.myvideoplatform.entity.vo.PaginationResultVO;
import com.myvideoplatform.exception.BusinessException;
import com.myvideoplatform.mappers.UserInfoMapper;
import com.myvideoplatform.service.UserInfoService;
import com.myvideoplatform.utils.CopyTools;
import com.myvideoplatform.utils.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {
    
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RedisComponent redisComponent;

    @Override
    public Integer deleteByUserId(String userId) {
        return userInfoMapper.deleteByUserId(userId);
    }

    @Override
    public Integer deleteByEmail(String email) {
        return userInfoMapper.deleteByEmail(email);
    }

    @Override
    public Integer deleteByNickName(String nickName) {
        return userInfoMapper.deleteByNickName(nickName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteByUserId(List<String> userIdList) {
        if (userIdList == null || userIdList.isEmpty()) {
            return 0;
        }
        return userInfoMapper.deleteBatch(userIdList);
    }

    @Override
    public Integer updateByUserId(UserInfo userInfo) {
        return userInfoMapper.updateByUserId(userInfo);
    }

    @Override
    public Integer updateByEmail(UserInfo userInfo) {
        return userInfoMapper.updateByEmail(userInfo);
    }

    @Override
    public Integer updateByNickName(UserInfo userInfo) {
        return userInfoMapper.updateByNickName(userInfo);
    }

    @Override
    public Integer updateByCondition(UserInfo userInfo, UserInfoQuery query) {
        return userInfoMapper.updateByCondition(userInfo, query);
    }

    @Override
    public UserInfo getUserInfoByUserId(String userId) {
        return userInfoMapper.selectByUserId(userId);
    }

    @Override
    public UserInfo getUserInfoByEmail(String email) {
        return userInfoMapper.selectByEmail(email);
    }

    @Override
    public UserInfo getUserInfoByNickName(String nickName) {
        return userInfoMapper.selectByNickName(nickName);
    }

    @Override
    public List<UserInfo> getUserInfoList() {
        return userInfoMapper.selectList();
    }

    @Override
    public List<UserInfo> getUserInfoListByCondition(UserInfoQuery query) {
        return userInfoMapper.selectListByCondition(query);
    }

    @Override
    public Long getUserInfoCountByCondition(UserInfoQuery query) {
        return userInfoMapper.selectCountByCondition(query);
    }
    
    @Override
    public PaginationResultVO<UserInfo> getUserInfoListByPage(UserInfoQuery query) {
        // 计算起始索引
        Integer pageNo = query.getPageNo() != null ? query.getPageNo() : 1;
        Integer pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        
        // 查询总数
        Long totalCount = userInfoMapper.selectCountByCondition(query);
        
        // 创建分页对象
        SimplePage simplePage = new SimplePage(pageNo, pageSize, totalCount);
        
        // 设置分页参数
        query.setPageNo(pageNo);
        query.setPageSize(pageSize);
        
        // 查询列表
        List<UserInfo> list = userInfoMapper.selectListByCondition(query);
        
        return new PaginationResultVO<>(simplePage, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(String email, String nickName, String registerPassword) {
        // 检查邮箱是否已存在
        UserInfo userInfo = this.userInfoMapper.selectByEmail(email);
        if (userInfo != null) {
            throw new BusinessException(ResponseCodeEnum.EMAIL_EXIST);
        }
        // 检查昵称是否已存在
        UserInfo nickNameUser = this.userInfoMapper.selectByNickName(nickName);
        if (nickNameUser != null) {
            throw new BusinessException(ResponseCodeEnum.NICK_NAME_EXIST);
        }
        // 生成用户ID
        String userId = StringTools.getRandomNumber(Constants.LENGTH_10);
        // 创建用户对象
        String encodedPassword = StringTools.encodeByMd5(registerPassword);
        UserInfo newUserInfo = new UserInfo(userId, email, nickName, encodedPassword);
        newUserInfo.setJoinTime(new Date());
        newUserInfo.setStatus(UserStatusEnum.NORMAL.getCode()); // 默认正常状态
        newUserInfo.setSex(SexEnum.UNKNOWN.getCode()); // 默认未知
        newUserInfo.setPersonIntroduction("这个人很懒，什么都没有留下");
        newUserInfo.setCoinCount(Constants.LENGTH_10); // 初始硬币
        newUserInfo.setCurrentCoinCount(Constants.LENGTH_10);
        newUserInfo.setTheme(0); // 默认主题
        // 插入数据
        userInfoMapper.insert(newUserInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TokenUserInfoDto login(String email, String password, String ip) {
        UserInfo userInfo = userInfoMapper.selectByEmail(email);
        if (userInfo == null) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_EXIST_OR_PASSWORD_ERROR);
        }
        if (!userInfo.getPassword().equals(StringTools.encodeByMd5(password))) {
            throw new BusinessException(ResponseCodeEnum.USER_NOT_EXIST_OR_PASSWORD_ERROR);
        }
        if (userInfo.getStatus().equals(UserStatusEnum.DISABLED.getCode())) {
            throw new BusinessException(ResponseCodeEnum.USER_DISABLED);
        }
        if (userInfo.getStatus().equals(UserStatusEnum.CANCELED.getCode())) {
            throw new BusinessException("用户已注销");
        }
        // 更新登录信息
        userInfo.setLastLoginTime(new Date());
        userInfo.setLastLoginIp(ip);
        userInfoMapper.updateByUserId(userInfo);
        
        // 生成token
        TokenUserInfoDto tokenUserInfoDto = new TokenUserInfoDto();
        tokenUserInfoDto.setUserId(userInfo.getUserId());
        tokenUserInfoDto.setNickName(userInfo.getNickName());
        tokenUserInfoDto.setAvatar(userInfo.getAvatar());
        tokenUserInfoDto.setEmail(userInfo.getEmail());
        return tokenUserInfoDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(String userId, String nickName, String avatar, String birthday, String school, String personIntroduction, String noticeInfo) {
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }
        if (nickName != null && !nickName.equals(userInfo.getNickName())) {
            UserInfo nickNameUser = userInfoMapper.selectByNickName(nickName);
            if (nickNameUser != null && !nickNameUser.getUserId().equals(userId)) {
                throw new BusinessException(ResponseCodeEnum.NICK_NAME_EXIST);
            }
        }
        userInfo.setNickName(nickName);
        userInfo.setAvatar(avatar);
        userInfo.setBirthday(birthday);
        userInfo.setSchool(school);
        userInfo.setPersonIntroduction(personIntroduction);
        userInfo.setNoticeInfo(noticeInfo);
        userInfoMapper.updateByUserId(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTheme(String userId, Integer theme) {
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }
        userInfo.setTheme(theme);
        userInfoMapper.updateByUserId(userInfo);
    }

    @Override
    public Long getUserCount() {
        UserInfoQuery query = new UserInfoQuery();
        return userInfoMapper.selectCountByCondition(query);
    }

    @Override
    public PaginationResultVO<UserInfo> loadUserByPage(UserInfoQuery query) {
        return getUserInfoListByPage(query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableUser(String userId, Integer status) {
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }
        userInfo.setStatus(status);
        userInfoMapper.updateByUserId(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String userId, String newPassword) {
        UserInfo userInfo = userInfoMapper.selectByUserId(userId);
        if (userInfo == null) {
            throw new BusinessException("用户不存在");
        }
        userInfo.setPassword(StringTools.encodeByMd5(newPassword));
        userInfoMapper.updateByUserId(userInfo);
    }

    @Override
    public void exportUser(HttpServletResponse response, UserInfoQuery query) {
        // TODO: 实现用户数据导出功能
    }
}