package com.kelab.usercenter.dal.dao;

import com.kelab.info.base.query.PageQuery;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.usercenter.dal.model.UserRankModel;
import com.kelab.usercenter.result.AcSubmitResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRankMapper {

    List<UserRankModel> queryPage(@Param("timeType") Integer timeType, @Param("pageQuery") PageQuery pageQuery);

    Integer total(@Param("timeType") Integer timeType);

    void delete(@Param("timeType") Integer timeType);

    AcSubmitResult queryCount(@Param("timeType") Integer timeType);
}
