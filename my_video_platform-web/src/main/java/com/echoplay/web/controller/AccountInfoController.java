package com.echoplay.web.controller;

import com.echoplay.entity.query.UserInfoQuery;
import com.echoplay.entity.vo.ResponseVO;
import com.echoplay.mappers.UserInfoMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("webAccountInfoController")
@RequestMapping("/account")
@Validated
public class AccountInfoController extends ABaseController {

    @Resource
    private UserInfoMapper userInfoMapper;

    @RequestMapping("/getUserCountInfo")
    public ResponseVO getUserCountInfo() {
        UserInfoQuery query = new UserInfoQuery();
        Long count = userInfoMapper.selectCountByCondition(query);
        return getSuccessResponseVO(count);
    }
}
