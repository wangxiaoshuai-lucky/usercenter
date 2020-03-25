package com.kelab.usercenter.serivce.impl;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.SingleResult;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.UserDayStatisticRepo;
import com.kelab.usercenter.dal.repo.UserLoginLogRepo;
import com.kelab.usercenter.result.OnlineUserResult;
import com.kelab.usercenter.serivce.OnlineService;
import com.kelab.usercenter.serivce.UserInfoService;
import com.kelab.usercenter.support.service.ProblemCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OnlineServiceImpl implements OnlineService {

    private final static double RATIO = 100000000.0;

    private final RedisCache redisCache;

    private final UserInfoService userInfoService;

    private final UserDayStatisticRepo userDayStatisticRepo;

    private final UserLoginLogRepo userLoginLogRepo;

    private final ProblemCenterService problemCenterService;

    @Autowired
    public OnlineServiceImpl(RedisCache redisCache,
                             UserInfoService userInfoService,
                             UserDayStatisticRepo userDayStatisticRepo,
                             UserLoginLogRepo userLoginLogRepo,
                             ProblemCenterService problemCenterService) {
        this.redisCache = redisCache;
        this.userInfoService = userInfoService;
        this.userDayStatisticRepo = userDayStatisticRepo;
        this.userLoginLogRepo = userLoginLogRepo;
        this.problemCenterService = problemCenterService;
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
        List<Integer> userIds = userSet.stream().map(Integer::parseInt).collect(Collectors.toList());
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

    @Override
    public SingleResult<Long> onlineCount(Context context) {
        // 首先刷新在线用户列表
        redisCache.removeRangeByScore(CacheBizName.ONLINE_USER, "", -1.0, System.currentTimeMillis() / RATIO);
        SingleResult<Long> result = new SingleResult<>();
        result.setObj(redisCache.zCard(CacheBizName.ONLINE_USER, ""));
        return result;
    }

    @Override
    public List<OnlineStatisticResult> queryOnlineStatistic(Context context, Integer type) {
        List<OnlineStatisticResult> result;
        switch (type) {
            case 0:
                result = userDayStatisticRepo.countYear();
                result.sort(Comparator.comparing(OnlineStatisticResult::getTime));
                break;
            case 1:
                result = userDayStatisticRepo.countMonth();
                result.sort(Comparator.comparing(OnlineStatisticResult::getTime));
                break;
            case 2:
                result = this.countDay(context);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return result;
    }

    private List<OnlineStatisticResult> countDay(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long endTime = calendar.getTimeInMillis();/*当前整点时间*/
        calendar.add(Calendar.HOUR, -24);
        long startTime = calendar.getTimeInMillis();/*昨天整点时间*/
        Map<String, OnlineStatisticResult> dayLogin = userLoginLogRepo.countDay(startTime, endTime)
                .stream().collect(Collectors.toMap(OnlineStatisticResult::getTime, obj -> obj, (v1, v2) -> v2));
        // 填充ac 和 submit
        Map<String, OnlineStatisticResult> daySubmit = problemCenterService.countDay(context, startTime, endTime);
        // 从昨天的今天整点渲染到
        List<OnlineStatisticResult> result = new ArrayList<>(24);
        // calendar 当前为昨天的整点
        for (int i = 0; i < 24; i++) {
            String time = calendar.get(Calendar.HOUR_OF_DAY) + "";
            if (time.length() == 1) {
                time = "0" + time;
            }
            OnlineStatisticResult record = new OnlineStatisticResult();
            record.setTime(time + ":00");
            OnlineStatisticResult loginRecord = dayLogin.get(time);
            if (loginRecord != null && loginRecord.getTotalLogin() != null) {
                record.setTotalLogin(loginRecord.getTotalLogin());
            }
            OnlineStatisticResult subRecord = daySubmit.get(time);
            if (subRecord != null && subRecord.getSubmitAll() != null) {
                record.setSubmitAll(subRecord.getSubmitAll());
            }
            if (subRecord != null && subRecord.getSubmitAc() != null) {
                record.setSubmitAc(subRecord.getSubmitAc());
            }
            result.add(record);
            calendar.add(Calendar.HOUR, 1);
        }
        return result;
    }
}
