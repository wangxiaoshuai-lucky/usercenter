package com.kelab.usercenter.serivce.impl;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.result.OnlineUserResult;
import com.kelab.usercenter.serivce.OnlineService;
import com.kelab.usercenter.serivce.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OnlineServiceImpl implements OnlineService {

    private final static double RATIO = 100000000.0;

    private final RedisCache redisCache;

    private final UserInfoService userInfoService;

    @Autowired
    public OnlineServiceImpl(RedisCache redisCache,
                             UserInfoService userInfoService) {
        this.redisCache = redisCache;
        this.userInfoService = userInfoService;
    }

    @Override
    public void online(Integer userId) {
        double score = (System.currentTimeMillis() + AppSetting.jwtMillisecond) / RATIO;
        redisCache.zAdd(CacheBizName.ONLINE_USER, "", String.valueOf(userId), score);
    }

    @Override
    public PaginationResult<OnlineUserResult> getOnlineUsers(Context context) {
        // 首先刷新在线用户列表
        redisCache.removeRangeByScore(CacheBizName.ONLINE_USER, "", -1.0, System.currentTimeMillis() / RATIO);
        PaginationResult<OnlineUserResult> result = new PaginationResult<>();
        Set<String> userSet = redisCache.zRange(CacheBizName.ONLINE_USER, "", 0L, -1L);
        if (CollectionUtils.isEmpty(userSet)) {
            result.setTotal(0);
            return result;
        }
        List<Integer> userIds = new ArrayList<>();
        userSet.forEach(item -> userIds.add(Integer.parseInt(item)));
        List<UserInfo> userInfos = userInfoService.queryByIds(context, userIds, false);
        List<OnlineUserResult> onlineUsers = new ArrayList<>();
        userInfos.forEach(item -> {
            OnlineUserResult single = new OnlineUserResult();
            single.setUserId(item.getId());
            single.setUsername(item.getUsername());
            onlineUsers.add(single);
        });
        result.setPagingList(onlineUsers);
        result.setTotal(onlineUsers.size());
        return result;
    }
}
