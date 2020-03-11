package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.UserInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    List<UserInfoModel> queryByIds(@Param("ids") List<Integer> id);

    UserInfoModel queryByUsername(@Param("username") String username);

    UserInfoModel queryByStudentId(@Param("studentId") String StudentId);

    Integer queryTotal();

    void save(@Param("record") UserInfoModel record);
}
