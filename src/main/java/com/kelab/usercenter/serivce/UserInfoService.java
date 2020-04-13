package com.kelab.usercenter.serivce;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.query.PageQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.info.usercenter.query.UserQuery;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.result.LoginResult;
import com.kelab.info.base.SingleResult;
import com.kelab.usercenter.result.AcSubmitResult;

import java.util.List;


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
    void resetPassword(Context context, Integer userId, String password);

    /**
     * 查询全部用户数量
     */
    SingleResult<Integer> queryTotal(Context context);

    /**
     * 获取用户提交排名
     */
    PaginationResult<UserInfo> submitStatistic(Context context, PageQuery pageQuery, TimeType timeType);

    /**
     * 分页查询
     */
    PaginationResult<UserInfo> queryPage(Context context, UserQuery query);


    /**
     * 通过 ids 查询
     */
    List<UserInfo> queryByIds(Context context, List<Integer> userIds, boolean withSubmitInfo);

    /**
     * 更新用户
     */
    String update(Context context, UserInfoDomain domain);

    /**
     * 删除用户
     */
    void delete(Context context, List<Integer> ids);

    /**
     * 今日AC/Submit量
     */
    AcSubmitResult queryTodayCount(Context context);

    /**
     * 判题结果更新
     */
    void judgeCallback(Context context, Integer userId, boolean ac);
}
