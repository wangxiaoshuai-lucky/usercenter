package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.dal.dao.UserLoginLogMapper;
import com.kelab.usercenter.dal.model.UserLoginLogModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.UserLoginLogRepo;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserLoginLogRepoImpl implements UserLoginLogRepo {

    private final UserLoginLogMapper userLoginLogMapper;

    private final RedisCache redisCache;

    @Autowired(required = false)
    public UserLoginLogRepoImpl(UserLoginLogMapper userLoginLogMapper,
                                RedisCache redisCache) {
        this.userLoginLogMapper = userLoginLogMapper;
        this.redisCache = redisCache;
    }

    @Override
    public Integer countByRange(Long startTime, Long endTime) {
        return userLoginLogMapper.countByRange(startTime, endTime);
    }

    @Override
    public void save(UserLoginLogModel record) {
        userLoginLogMapper.save(record);
    }

    @Override
    public List<OnlineStatisticResult> countDay(Long startTime, Long endTime) {
        String cacheKey = "DAY::" + startTime + "::" + endTime;
        String cache = redisCache.cacheOne(CacheBizName.USER_DAY_STATISTIC, cacheKey,
                String.class, missKey -> {
                    List<OnlineStatisticResult> dbObjs = userLoginLogMapper.countDay(startTime, endTime);
                    return JSON.toJSONString(dbObjs);
                });
        return JSON.parseArray(cache, OnlineStatisticResult.class);
    }
}
