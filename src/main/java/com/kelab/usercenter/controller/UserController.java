package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.query.PageQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.StatusMsgConstant;
import com.kelab.usercenter.constant.UserInfoConstant;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.resultVO.SingleResult;
import com.kelab.usercenter.serivce.OnlineService;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private UserInfoService userInfoService;

    private OnlineService onlineService;

    @Autowired
    public UserController(UserInfoService userInfoService, OnlineService onlineService) {
        this.userInfoService = userInfoService;
        this.onlineService = onlineService;
    }


    /**
     * 登录接口
     */
    @GetMapping("/user/signin.do")
    @Verify(notNull = {"username", "password", "verifyCode", "uuid"})
    public JsonAndModel login(Context context, String username, String password, String verifyCode, String uuid) {
        LoginResult result = userInfoService.login(context, username, password, verifyCode, uuid);
        JsonAndModel.Builder builder = JsonAndModel.builder(result.getStatus()).data(result);
        if (result.getStatus().equals(StatusMsgConstant.LOGIN_SUCCESS)) {
            builder.token(tokens(result));
        }
        return builder.build();
    }

    /**
     * 用户注册
     */
    @PostMapping("/user.do")
    @Verify(notNull = {"userInfo.username", "userInfo.password", "userInfo.realName", "userInfo.studentId", "userInfo.email"})
    public JsonAndModel register(String logId, Integer operatorId, @RequestBody UserInfo userInfo) {
        Context context = new Context();
        context.setLogId(logId);
        context.setOperatorId(operatorId);
        LoginResult result = userInfoService.register(context, userInfo);
        JsonAndModel.Builder builder = JsonAndModel.builder(result.getStatus()).data(result);
        if (result.getStatus().equals(StatusMsgConstant.LOGIN_SUCCESS)) {
            builder.token(tokens(result));
        }
        return builder.build();
    }


    /**
     * 用户总数接口
     */
    @GetMapping("/user/total.do")
    public JsonAndModel countTotalUser(Context context) {
        SingleResult<Integer> sr = this.userInfoService.queryTotal(context);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).data(sr).build();
    }

    /**
     * 找回密码
     */
    @GetMapping("/user/resetPasswd.do")
    @Verify(notNull = {"username", "verifyCode", "uuid"})
    public JsonAndModel resetPwd(Context context, String username,String verifyCode,String uuid){
        String status = userInfoService.resetPwd(context, username, verifyCode, uuid);
        return JsonAndModel.builder(status).build();
    }

    /**
     * 修改密码
     */
    @PutMapping("/user/resetPasswd.do")
    @Verify(notNull = "newPassword")
    public JsonAndModel resetPwd(String logId, Integer operatorId, @RequestBody String newPassword) {
        Context context = new Context();
        context.setLogId(logId);
        context.setOperatorId(operatorId);
        userInfoService.resetPassword(context, newPassword);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 提交排行榜
     */
    @GetMapping("/user/submit/statistic.do")
    public JsonAndModel submitStatistic(Context context, PageQuery pageQuery, Integer timeType) throws IllegalAccessException {
        PaginationResult<UserInfo> result = userInfoService.submitStatistic(context, pageQuery, TimeType.valueOf(timeType));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).data(result).build();
    }

    private String tokens(LoginResult result) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserInfoConstant.USER_ID, result.getUserId());
        claims.put(UserInfoConstant.ROLE_ID, result.getRoleId());
        return TokenUtil.tokens(claims
                , AppSetting.secretKey
                , AppSetting.jwtMillisecond
                , AppSetting.jwtIssuer
                , AppSetting.jwtAud);
    }
}
