package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.UserSubmitInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserSubmitInfoMapper {

    UserSubmitInfoModel queryByUserId(@Param("userId") Integer userId);

}
