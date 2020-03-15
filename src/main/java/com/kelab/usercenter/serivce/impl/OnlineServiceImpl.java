package com.kelab.usercenter.serivce.impl;

import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.serivce.OnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class OnlineServiceImpl implements OnlineService {

    private final RedisCache redisCache;

    @Autowired
    public OnlineServiceImpl(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    @Override
    public void online(Integer userId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MILLISECOND, AppSetting.jwtMillisecond);
        double score = calendar.getTimeInMillis();
        redisCache.zAdd(CacheBizName.ONLINE_USER, "", String.valueOf(userId), score);
    }
}
