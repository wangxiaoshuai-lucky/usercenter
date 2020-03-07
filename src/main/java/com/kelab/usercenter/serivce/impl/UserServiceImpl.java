package com.kelab.usercenter.serivce.impl;

import com.kelab.info.base.constant.BaseRetCodeConstant;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.md5.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserInfoService {

    private final UserInfoRepo userInfoRepo;

    @Autowired(required = false)
    public UserServiceImpl(UserInfoRepo userInfoRepo) {
        this.userInfoRepo = userInfoRepo;
    }

    @Override
    public LoginResult login(Context context, String username, String password, String verifyCode, String uuid) {
        LoginResult result = new LoginResult();
        // todo 验证码验证
        UserInfoDomain domain = userInfoRepo.queryByUsername(username, false);
        if (domain == null) {
            result.setStatus(BaseRetCodeConstant.USER_NOT_EXIST_ERROR);
        } else if (!Md5Util.StringInMd5(password).equals(domain.getPassword())) {
            result.setStatus(BaseRetCodeConstant.PWD_ERROR);
        } else {
            result.setStatus(BaseRetCodeConstant.LOGIN_SUCCESS);
            result.setAvatar(domain.getAvatar());
            result.setUsername(domain.getUsername());
            result.setRoleId(domain.getRoleId());
            result.setUserId(domain.getId());
            // todo 登录日志
        }
        return result;
    }
}
