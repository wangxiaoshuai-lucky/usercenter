package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.UserSubmitInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserSubmitInfoMapper {

    List<UserSubmitInfoModel> queryByUserIds(@Param("userIds") List<Integer> userId);

    Integer save(@Param("record") UserSubmitInfoModel record);

    void updateByUserId(@Param("record") UserSubmitInfoModel record);

    void delete(@Param("userIds") List<Integer> userIds);
}
