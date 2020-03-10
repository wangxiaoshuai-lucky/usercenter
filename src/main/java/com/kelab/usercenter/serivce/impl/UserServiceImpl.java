package com.kelab.usercenter.serivce.impl;

import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.builder.UserInfoBuilder;
import com.kelab.usercenter.constant.CacheConstant;
import com.kelab.usercenter.constant.StatusMsgConstant;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.model.UserLoginLogModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import com.kelab.usercenter.dal.repo.UserLoginLogRepo;
import com.kelab.usercenter.resultVO.SingleResult;
import com.kelab.usercenter.serivce.OnlineService;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.md5.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserInfoService {

    private UserInfoRepo userInfoRepo;

    private RedisCache redisCache;

    private UserLoginLogRepo userLoginLogRepo;

    private OnlineService onlineService;

    @Autowired(required = false)
    public UserServiceImpl(UserInfoRepo userInfoRepo
            , RedisCache redisCache
            , UserLoginLogRepo userLoginLogRepo
            , OnlineService onlineService) {
        this.userInfoRepo = userInfoRepo;
        this.redisCache = redisCache;
        this.userLoginLogRepo = userLoginLogRepo;
        this.onlineService = onlineService;
    }

    @Override
    public LoginResult login(Context context, String username, String password, String verifyCode, String uuid) {
        LoginResult result = new LoginResult();
        if (!verifyCode.equalsIgnoreCase(redisCache.get(CacheConstant.VERIFY_CODE, uuid))) {
            result.setStatus(StatusMsgConstant.VERIFY_CODE_ERROR);
            return result;
        }
        // 校验验证码后验证码失效
        redisCache.delete(CacheConstant.VERIFY_CODE, uuid);
        UserInfoDomain domain = userInfoRepo.queryByUsername(username, false);
        if (domain == null) {
            result.setStatus(StatusMsgConstant.USER_NOT_EXIST_ERROR);
        } else if (!Md5Util.StringInMd5(password).equals(domain.getPassword())) {
            result.setStatus(StatusMsgConstant.PWD_ERROR);
        } else {
            result = loginSuccess(domain);
        }
        return result;
    }

    @Override
    public LoginResult register(Context context, UserInfo userInfo) {
        LoginResult result = new LoginResult();
        UserInfoDomain userInfoDomain = UserInfoBuilder.buildNewUser(userInfo);
        // 密码 md5 加密一次
        userInfoDomain.setPassword(Md5Util.StringInMd5(userInfoDomain.getPassword()));
        // username 查重
        if (userInfoRepo.queryByUsername(userInfoDomain.getUsername(), false) != null) {
            result.setStatus(StatusMsgConstant.USER_EXISTED_ERROR);
            return result;
        }
        // stuId 查重
        if (userInfoRepo.queryByStudentId(userInfoDomain.getStudentId(), false) != null) {
            result.setStatus(StatusMsgConstant.STUDENT_ID_EXISTED_ERROR);
            return result;
        }
        userInfoRepo.save(userInfoDomain);
        return loginSuccess(userInfoDomain);
    }

    @Override
    public SingleResult<Integer> queryTotal(Context context) {
        return new SingleResult<>(userInfoRepo.queryTotal());
    }

    private LoginResult loginSuccess(UserInfoDomain domain) {
        LoginResult result = new LoginResult();
        result.setStatus(StatusMsgConstant.LOGIN_SUCCESS);
        result.setAvatar(domain.getAvatar());
        result.setUsername(domain.getUsername());
        result.setRoleId(domain.getRoleId());
        result.setUserId(domain.getId());
        userLoginLogRepo.save(new UserLoginLogModel(null, domain.getId(), System.currentTimeMillis()));
        onlineService.online(result.getUserId());
        return result;
    }
}
