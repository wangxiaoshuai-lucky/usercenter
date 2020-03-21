package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.constant.JsonWebTokenConstant;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.base.query.PageQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.serivce.OnlineService;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.util.token.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
public class OnlineController {

    private OnlineService onlineService;

    private UserInfoService userInfoService;

    @Autowired
    public OnlineController(OnlineService onlineService, UserInfoService userInfoService) {
        this.onlineService = onlineService;
        this.userInfoService = userInfoService;
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
     * 今日AC/Submit量
     */
    @GetMapping("/submit/todayCount.do")
    public JsonAndModel submitStatistic(Context context) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).data(userInfoService.queryTodayCount(context)).build();
    }

    /**
     * 在线用户列表
     */
    @GetMapping("/user/online.do")
    public JsonAndModel onlineUserId(Context context) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(onlineService.getOnlineUsers(context))
                .build();
    }

    /**
     * 在线总人数
     */
    @GetMapping("/user/online/total.do")
    public JsonAndModel onlineCount(Context context) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(this.onlineService.onlineCount(context))
                .build();
    }


    /**
     * 刷新 token
     */
    @GetMapping("/user/jwt/refresh.do")
    public JsonAndModel refreshJwt(Context context) {
        if (context.getFreshExp() < System.currentTimeMillis()) {
            return JsonAndModel.builder(StatusMsgConstant.JWT_IS_EXPIRE_ERROR).build();
        }
        // 重建token
        UserInfo userInfo = userInfoService.queryByIds(context, Collections.singletonList(context.getOperatorId()), false).get(0);
        onlineService.online(userInfo.getId());
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .token(refreshToken(context, userInfo))
                .build();
    }

    private String refreshToken(Context context, UserInfo userInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JsonWebTokenConstant.USER_ID, userInfo.getId());
        claims.put(JsonWebTokenConstant.ROLE_ID, userInfo.getRoleId());
        claims.put(JsonWebTokenConstant.USERNAME, userInfo.getUsername());
        claims.put(JsonWebTokenConstant.REFRESH_EXP_DATE, context.getFreshExp());
        return TokenUtil.tokens(claims
                , AppSetting.secretKey
                , AppSetting.jwtMillisecond
                , AppSetting.jwtIssuer
                , AppSetting.jwtAud);
    }
}
