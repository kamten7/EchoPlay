package com.myvideoplatform.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
//用户信息
@Data
public class UserInfo implements Serializable {
    // 用户ID
    private String userId;
    //昵称
    private String nickName;
    //邮箱
    private String email;
    // 密码
    private String password;
    //头像
    private String avatar;
    // 性别
    private  Integer sex;
    //生日
    private String birthday;
    //学校
    private String school;
    //个人简�?    private String personIntroduction;
    //加入时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date joinTime;
    //最后登录时�?    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    //最后登录IP
    private String lastLoginIp;
    //用户状�?    private Integer status;
    //空间公告
    private String noticeInfo;
    //硬币总数
    private Integer coinCount;
    //当前硬币�?    private Integer currentCoinCount;
    //主题
    private int theme;

    // 无参构造函�?MyBatis需�?
    public UserInfo() {
    }

    public UserInfo(String s, String email, String nickName, String registerPassword) {
        this.userId = s;
        this.email = email;
        this.nickName = nickName;
        this.password = registerPassword;
    }
}
