package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.BaseRetCodeConstant;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.usercenter.UserCenterApplication;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    @RequestMapping(value = "/user/signin.do", method = {RequestMethod.GET})
    @Verify(notNull = {"username", "password", "verifyCode", "uuid"})
    public JsonAndModel login(Context context, String username, String password, String verifyCode, String uuid) {
        LoginResult result = userInfoService.login(context, username, password, verifyCode, uuid);
        JsonAndModel.Builder builder = JsonAndModel.builder(BaseRetCodeConstant.SUCCESS).data(result);
        if (result.getStatus().equals(BaseRetCodeConstant.LOGIN_SUCCESS)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("user_id", result.getUserId());
            claims.put("role_id", result.getRoleId());
            String token = TokenUtil.tokens(claims
                    , UserCenterApplication.appSetting.secretKey
                    , UserCenterApplication.appSetting.millisecond
                    , UserCenterApplication.appSetting.jwtIssuer
                    , UserCenterApplication.appSetting.jwtAud);
            builder.token(token);
        }
        // todo 上线人数记录
        return builder.build();
    }
}
