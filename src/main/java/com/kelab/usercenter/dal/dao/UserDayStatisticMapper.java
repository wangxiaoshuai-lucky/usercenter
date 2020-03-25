package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.UserDayStatisticModel;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDayStatisticMapper {

    /**
     * 插入一天的记录
     */
    void save(@Param("record") UserDayStatisticModel model);

    /**
     * 根据每天的来算
     */
    List<OnlineStatisticResult> countMonth(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 根据每月的来算
     */
    List<OnlineStatisticResult> countYear(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
