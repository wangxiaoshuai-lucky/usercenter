package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.model.UserDayStatisticModel;
import com.kelab.info.usercenter.info.OnlineStatisticResult;

import java.util.List;

public interface UserDayStatisticRepo {

    /**
     * 插入一天的记录
     */
    void save(UserDayStatisticModel model);

    /**
     * 根据每天的来算, 走缓存
     * 上个月的昨天 ---> 昨天23:59
     */
    List<OnlineStatisticResult> countMonth();

    /**
     * 根据每月的来算，走缓存
     * 去年的昨天 ---> 昨天23:59
     */
    List<OnlineStatisticResult> countYear();
}
