package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.BaseRetCodeConstant;
import com.kelab.usercenter.repo.model.UserInfo;
import com.kelab.usercenter.serivce.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserInfoService userInfoService;

    @Autowired
    public UserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }


    @RequestMapping(value = "/user/signin.do", method = {RequestMethod.GET})
    @Verify(notNull = {"username", "password", "verifyCode", "uuid"})
    public JsonAndModel login(String username, String password, String verifyCode, String uuid) {
        UserInfo userInfo = userInfoService.login(username, password);
        return JsonAndModel.builder(BaseRetCodeConstant.SUCCESS).data(userInfo).build();
    }
}
