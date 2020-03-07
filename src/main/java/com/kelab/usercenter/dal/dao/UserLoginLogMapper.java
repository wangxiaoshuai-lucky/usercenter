package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.UserLoginLogModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UserLoginLogMapper {

    void save(@Param("record") UserLoginLogModel record);

}
