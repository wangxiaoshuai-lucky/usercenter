package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.constant.UserRoleConstant;
import com.kelab.info.base.query.PageQuery;
import com.kelab.info.base.query.UserQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.StatusMsgConstant;
import com.kelab.usercenter.constant.UserInfoConstant;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.convert.UserInfoConvert;
import com.kelab.usercenter.result.SingleResult;
import com.kelab.usercenter.serivce.OnlineService;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public JsonAndModel register(Context context, @RequestBody UserInfo userInfo) {
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
    public JsonAndModel resetPwdEmail(Context context, String username, String verifyCode, String uuid) {
        String status = userInfoService.resetPwd(context, username, verifyCode, uuid);
        return JsonAndModel.builder(status).build();
    }

    /**
     * 修改密码
     */
    @PutMapping("/user/resetPasswd.do")
    @Verify(notNull = "newPassword")
    public JsonAndModel resetPwd(Context context, @RequestBody String newPassword) {
        userInfoService.resetPassword(context, context.getOperatorId(), newPassword);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 提交排行榜
     */
    @GetMapping("/user/submit/statistic.do")
    @Verify(notNull = {"pageQuery.page", "pageQuery.rows"})
    public JsonAndModel submitStatistic(Context context, PageQuery pageQuery, Integer timeType) {
        PaginationResult<UserInfo> result = userInfoService.submitStatistic(context, pageQuery, TimeType.valueOf(timeType));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).data(result).build();
    }

    /**
     * 查询用户
     */
    @GetMapping("/user.do")
    public JsonAndModel queryPage(Context context, UserQuery query) {
        if (context.getOperatorRoleId() != null && context.getOperatorRoleId() > UserRoleConstant.TEACHER &&
                (query.getStudentId() != null || query.getRealName() != null || query.getRoleId() != null)) {
            return JsonAndModel.builder(StatusMsgConstant.SUCCESS).data(StatusMsgConstant.ILLEGAL_ACCESS_ERROR).build();
        }
        PaginationResult<UserInfo> result = userInfoService.queryPage(context, query);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).data(result).build();
    }

    /**
     * 更新用户
     */
    @PutMapping("/user.do")
    @Verify(notNull = {"context.operatorRoleId", "context.operatorId", "userInfo.id"})
    public JsonAndModel update(Context context, @RequestBody UserInfo userInfo) {
        if (isIllegal(context, userInfo)) {
            return JsonAndModel.builder(StatusMsgConstant.ILLEGAL_ACCESS_ERROR).build();
        }
        return JsonAndModel.builder(userInfoService.update(context, UserInfoConvert.infoToDomain(userInfo))).build();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/user.do")
    @Verify(notNull = {"uiids"})
    public JsonAndModel delete(Context context, String uiids) {
        String[] strIds = uiids.split(",");
        List<Integer> ids = new ArrayList<>(strIds.length);
        for (String str : strIds) {
            ids.add(Integer.parseInt(str));
        }
        userInfoService.delete(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }


    private boolean isIllegal(Context context, UserInfo userInfo) {
        // 非管理员不能修改roleId, password, 以及其他人的信息
        if (context.getOperatorRoleId() != UserRoleConstant.ADMIN) {
            return userInfo.getRoleId() != null || userInfo.getPassword() != null
                    || !context.getOperatorId().equals(userInfo.getId());
        } else {
            return false;
        }
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
