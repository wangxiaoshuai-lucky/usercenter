package com.kelab.usercenter.serivce;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.SingleResult;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.usercenter.result.OnlineUserResult;

import java.util.List;

public interface OnlineService {

    /**
     * 用户上线
     */
    void online(Integer userId);

    /**
     * 获取在线用户
     */
    PaginationResult<OnlineUserResult> getOnlineUsers(Context context);

    /**
     * 在线人数
     */
    SingleResult<Long> onlineCount(Context context);

    /**
     * 近段时间（小时，天，月）的 ac、submit、login 数据
     * type  0  按年查询 返回一年内每月的数据
     * type  1  按月查询 返回一月内每天的数据
     * type  2  按日查询 返回一天内每小时的数据
     */
    List<OnlineStatisticResult> queryOnlineStatistic(Context context, Integer type);
}
