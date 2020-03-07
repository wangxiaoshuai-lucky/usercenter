package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.UserInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {

    UserInfoModel queryById(@Param("id") Integer id);

    UserInfoModel queryByUsername(@Param("username") String username);

}
