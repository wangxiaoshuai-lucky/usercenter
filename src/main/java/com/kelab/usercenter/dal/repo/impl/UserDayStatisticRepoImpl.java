package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.dal.dao.UserDayStatisticMapper;
import com.kelab.usercenter.dal.model.UserDayStatisticModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.UserDayStatisticRepo;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public class UserDayStatisticRepoImpl implements UserDayStatisticRepo {

    private UserDayStatisticMapper userDayStatisticMapper;

    private RedisCache redisCache;

    @Autowired(required = false)
    public UserDayStatisticRepoImpl(UserDayStatisticMapper userDayStatisticMapper,
                                    RedisCache redisCache) {
        this.userDayStatisticMapper = userDayStatisticMapper;
        this.redisCache = redisCache;
    }

    @Override
    public void save(UserDayStatisticModel model) {
        userDayStatisticMapper.save(model);
    }

    @Override
    public List<OnlineStatisticResult> countMonth() {
        long endTime = getYesterdayZeroTime();
        long startTime = getPreMonthZeroTime();
        String cacheKey = "MONTH::" + startTime + "::" + endTime;
        String cache = redisCache.cacheOne(CacheBizName.USER_DAY_STATISTIC, cacheKey,
                String.class, missKey -> {
                    List<OnlineStatisticResult> dbObjs = userDayStatisticMapper.countMonth(startTime, endTime);
                    return JSON.toJSONString(dbObjs);
                });
        return JSON.parseArray(cache, OnlineStatisticResult.class);
    }

    @Override
    public List<OnlineStatisticResult> countYear() {
        long endTime = getYesterdayZeroTime();
        long startTime = getPreYearZeroTime();
        String cacheKey = "YEAR::" + startTime + "::" + endTime;
        String cache = redisCache.cacheOne(CacheBizName.USER_DAY_STATISTIC, cacheKey,
                String.class, missKey -> {
                    List<OnlineStatisticResult> dbObjs = userDayStatisticMapper.countYear(startTime, endTime);
                    return JSON.toJSONString(dbObjs);
                });
        return JSON.parseArray(cache, OnlineStatisticResult.class);
    }

    private long getYesterdayZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    private long getPreMonthZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime().getTime();
    }

    private long getPreYearZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime().getTime();
    }
}
