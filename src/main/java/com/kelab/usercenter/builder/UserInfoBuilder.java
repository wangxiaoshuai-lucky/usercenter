package com.kelab.usercenter.builder;

import com.kelab.info.base.constant.UserRoleConstant;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.usercenter.constant.UserInfoConstant;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;

public class UserInfoBuilder {

    public static UserInfoDomain buildNewUser(UserInfo userInfo) {
        UserInfoDomain domain = new UserInfoDomain();
        domain.setUsername(userInfo.getUsername());
        domain.setPassword(userInfo.getPassword());
        domain.setEmail(userInfo.getEmail());
        domain.setRealName(userInfo.getRealName());
        domain.setStudentId(userInfo.getStudentId());
        domain.setRoleId(UserRoleConstant.STUDENT);
        domain.setAvatar(UserInfoConstant.DEFAULT_AVATAR);
        UserSubmitInfoDomain submitInfoDomain = new UserSubmitInfoDomain();
        submitInfoDomain.setSubmitNum(0);
        submitInfoDomain.setAcNum(0);
        domain.setSubmitInfo(submitInfoDomain);
        return domain;
    }
}
