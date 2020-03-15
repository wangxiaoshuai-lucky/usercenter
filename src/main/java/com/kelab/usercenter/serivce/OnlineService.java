package com.kelab.usercenter.serivce;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.usercenter.result.OnlineUserResult;

public interface OnlineService {

    /**
     * 用户上线
     */
    void online(Integer userId);

    /**
     * 获取在线用户
     */
    PaginationResult<OnlineUserResult> getOnlineUsers(Context context);
}
