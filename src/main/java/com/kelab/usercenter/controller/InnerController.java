package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.usercenter.serivce.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InnerController {

    private UserInfoService userInfoService;

    public InnerController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @GetMapping("/inner/queryByIds")
    @Verify(sizeLimit = "ids [1, 200]")
    public List<UserInfo> queryByIds(Context context, @RequestParam(value = "ids") List<Integer> ids) {
        return userInfoService.queryByIds(context, ids, false);
    }

    @GetMapping("/inner/judgeCallback")
    @Verify(notNull = "*")
    public Object judgeCallback(Context context, Integer userId, boolean ac) {
        userInfoService.judgeCallback(context, userId, ac);
        return "ok";
    }
}
