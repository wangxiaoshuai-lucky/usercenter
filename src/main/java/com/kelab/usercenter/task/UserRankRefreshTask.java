package com.kelab.usercenter.task;

import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.constant.enums.DistributeKey;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.dal.model.UserDayStatisticModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.redis.lock.RedisLock;
import com.kelab.usercenter.dal.repo.UserDayStatisticRepo;
import com.kelab.usercenter.dal.repo.UserLoginLogRepo;
import com.kelab.usercenter.dal.repo.UserRankRepo;
import com.kelab.usercenter.result.AcSubmitResult;
import com.kelab.util.uuid.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Calendar;
import java.util.Date;

@Configuration
@EnableScheduling
public class UserRankRefreshTask {

    private final Logger logger = LoggerFactory.getLogger(UserRankRefreshTask.class);

    /**
     * 定时任务： 表达式预览：https://www.pppet.net/
     */

    private UserRankRepo userRankRepo;

    private UserLoginLogRepo userLoginLogRepo;

    private UserDayStatisticRepo userDayStatisticRepo;

    private RedisLock redisLock;

    private RedisCache redisCache;

    public UserRankRefreshTask(UserRankRepo userRankRepo,
                               UserLoginLogRepo userLoginLogRepo,
                               UserDayStatisticRepo userDayStatisticRepo,
                               RedisLock redisLock,
                               RedisCache redisCache) {
        this.userRankRepo = userRankRepo;
        this.userLoginLogRepo = userLoginLogRepo;
        this.userDayStatisticRepo = userDayStatisticRepo;
        this.redisLock = redisLock;
        this.redisCache = redisCache;
    }

//    @Scheduled(cron = "0/1 * * * * ?")
//    private void test() {
//        logger.info("定时任务执行......., 当前时间:{}", new Date());
//    }

    /**
     * 统计今日login、ac、submit
     * 由于多机部署，需要上分布式锁
     */
    @Scheduled(cron = "0 0 0 * * ?")
    private void refreshDayRank() {
        String uuid = UuidUtil.genUUID();
        if (redisLock.lock(DistributeKey.USER_DAY_STATISTIC, uuid, 2000, 1)) {
            // 检测是否已经被其他机器完成统计
            if (redisCache.get(CacheBizName.USER_DAY_STATISTIC_REFRESH_END, "refreshDayRank") != null) {
                redisLock.unLock(DistributeKey.USER_DAY_STATISTIC, uuid);
                return;
            }
            // 开始统计
            saveDayStatistic();
            userRankRepo.delete(TimeType.DAY);
            // 完成统计，解锁
            logger.info("刷新每日榜单......., 当前时间:{}", new Date());
            redisCache.set(CacheBizName.USER_DAY_STATISTIC_REFRESH_END, "refreshDayRank", uuid);
            redisLock.unLock(DistributeKey.USER_DAY_STATISTIC, uuid);
        }
    }

    /**
     * 每天0点统计上一天的数据
     */
    private void saveDayStatistic() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long endTime = calendar.getTimeInMillis();/*当前整点时间*/
        calendar.add(Calendar.HOUR, -24);
        long startTime = calendar.getTimeInMillis();/*昨天整点时间*/
        UserDayStatisticModel model = new UserDayStatisticModel();
        AcSubmitResult result = userRankRepo.queryCount(TimeType.DAY);
        model.setLoginNum(userLoginLogRepo.countByRange(startTime, endTime));
        model.setAcNum(result.getAc());
        model.setSubmitNum(result.getSubmit());
        model.setRecordTime(System.currentTimeMillis());
        userDayStatisticRepo.save(model);
    }

    @Scheduled(cron = "0 0 0 ? * 1")
    private void refreshWeekRank() {
        logger.info("刷新每周榜单......., 当前时间:{}", new Date());
        userRankRepo.delete(TimeType.DAY);
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    private void refreshMonthRank() {
        logger.info("刷新每月榜单......., 当前时间:{}", new Date());
        userRankRepo.delete(TimeType.MONTH);
    }
}
