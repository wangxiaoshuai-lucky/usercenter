package com.kelab.usercenter.serivce.impl;

import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.usercenter.constant.CacheConstant;
import com.kelab.usercenter.constant.OjConstant;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.model.UserLoginLogModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import com.kelab.usercenter.dal.repo.UserLoginLogRepo;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.md5.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserInfoService {

    private UserInfoRepo userInfoRepo;

    private RedisCache redisCache;

    private UserLoginLogRepo userLoginLogRepo;

    @Autowired(required = false)
    public UserServiceImpl(UserInfoRepo userInfoRepo
            , RedisCache redisCache
            , UserLoginLogRepo userLoginLogRepo) {
        this.userInfoRepo = userInfoRepo;
        this.redisCache = redisCache;
        this.userLoginLogRepo = userLoginLogRepo;
    }

    @Override
    public LoginResult login(Context context, String username, String password, String verifyCode, String uuid) {
        LoginResult result = new LoginResult();
        if (!verifyCode.equalsIgnoreCase(redisCache.get(CacheConstant.VERIFY_CODE, uuid))) {
            result.setStatus(OjConstant.VERIFY_CODE_ERROR);
            return result;
        }
        // 校验验证码后验证码失效
        redisCache.delete(CacheConstant.VERIFY_CODE, uuid);
        UserInfoDomain domain = userInfoRepo.queryByUsername(username, false);
        if (domain == null) {
            result.setStatus(OjConstant.USER_NOT_EXIST_ERROR);
        } else if (!Md5Util.StringInMd5(password).equals(domain.getPassword())) {
            result.setStatus(OjConstant.PWD_ERROR);
        } else {
            result.setStatus(OjConstant.LOGIN_SUCCESS);
            result.setAvatar(domain.getAvatar());
            result.setUsername(domain.getUsername());
            result.setRoleId(domain.getRoleId());
            result.setUserId(domain.getId());
            userLoginLogRepo.save(new UserLoginLogModel(null, domain.getId(), System.currentTimeMillis()));
        }
        return result;
    }
}
