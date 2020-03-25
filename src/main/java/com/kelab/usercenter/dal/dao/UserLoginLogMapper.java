package com.kelab.usercenter.dal.dao;

import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.usercenter.dal.model.UserLoginLogModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserLoginLogMapper {

    void save(@Param("record") UserLoginLogModel record);

    /**
     * 根据每天的来算
     */
    List<OnlineStatisticResult> countDay(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    /**
     * 范围查询登录条数
     */
    Integer countByRange(@Param("startTime") Long startTime, @Param("endTime") Long endTime);
}
