package com.kelab.usercenter.serivce;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.query.PageQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;
import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.resultVO.SingleResult;

public interface UserInfoService {

    /**
     * 登录
     */
    LoginResult login(Context context, String username, String password, String verifyCode, String uuid);

    /**
     * 注册
     */
    LoginResult register(Context context, UserInfo userInfo);

    /**
     * 重置密码
     */
    String resetPwd(Context context, String username, String verifyCode, String uuid);

    /**
     * 重置密码
     */
    void resetPassword(Context context, String password);

    /**
     * 查询全部用户数量
     */
    SingleResult<Integer> queryTotal(Context context);

    /**
     * 获取用户提交排名
     */
    PaginationResult<UserInfo> submitStatistic(Context context, PageQuery pageQuery, TimeType timeType);

}
