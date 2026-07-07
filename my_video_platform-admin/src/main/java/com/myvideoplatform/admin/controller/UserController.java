package com.myvideoplatform.admin.controller;

import com.myvideoplatform.entity.query.UserInfoQuery;
import com.myvideoplatform.entity.vo.ResponseVO;
import com.myvideoplatform.service.UserInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@RestController("adminUserController")
@RequestMapping("/user")
@Validated
public class UserController extends ABaseController {

    @Resource
    private UserInfoService userInfoService;

    @RequestMapping("/loadUser")
    public ResponseVO loadUser(Integer pageNo,
                               Integer pageSize,
                               String nickNameFuzzy,
                               Integer status) {
        UserInfoQuery query = new UserInfoQuery();
        query.setPageNo(pageNo != null ? pageNo : 1);
        query.setPageSize(pageSize != null ? pageSize : 20);
        query.setNickName(nickNameFuzzy);
        query.setStatus(status);
        return getSuccessResponseVO(userInfoService.loadUserByPage(query));
    }

    @RequestMapping("/changeStatus")
    public ResponseVO changeStatus(String userId, @NotNull Integer status) {
        userInfoService.changeUserStatus(userId, status);
        return getSuccessResponseVO(null);
    }
}
