package com.kelab.usercenter.task;

import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.dal.repo.UserRankRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

@Configuration
@EnableScheduling
public class UserRankRefreshTask {

    private final Logger logger = LoggerFactory.getLogger(UserRankRefreshTask.class);

    /**
     * 定时任务： 表达式预览：https://www.pppet.net/
     */

    private UserRankRepo userRankRepo;

    public UserRankRefreshTask(UserRankRepo userRankRepo) {
        this.userRankRepo = userRankRepo;
    }

//    @Scheduled(cron = "0/1 * * * * ?")
//    private void test() {
//        logger.info("定时任务执行......., 当前时间:{}", new Date());
//    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void refreshDayRank() {
        logger.info("刷新每日榜单......., 当前时间:{}", new Date());
        userRankRepo.delete(TimeType.DAY);
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
