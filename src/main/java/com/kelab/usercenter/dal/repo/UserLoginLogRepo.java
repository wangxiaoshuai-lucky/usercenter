package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.model.UserLoginLogModel;
import com.kelab.info.usercenter.info.OnlineStatisticResult;

import java.util.List;

public interface UserLoginLogRepo {

    /**
     * 保存登录日志
     *
     * @param record .
     */
    void save(UserLoginLogModel record);

    /**
     * 查询今日登录数量
     */
    Integer countByRange(Long startTime, Long endTime);


    /**
     * 获取每个小时的登录情况
     * 走缓存，endTime当前的整点时间, startTime昨天的整点时间
     */
    List<OnlineStatisticResult> countDay(Long startTime, Long endTime);
}
