package com.kelab.usercenter.dal.dao;

import com.kelab.info.base.query.UserQuery;
import com.kelab.usercenter.dal.model.UserInfoModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    List<UserInfoModel> queryByIds(@Param("ids") List<Integer> id);

    List<UserInfoModel> queryPage(@Param("query") UserQuery query);

    Integer queryTotal(@Param("query") UserQuery query);

    UserInfoModel queryByUsername(@Param("username") String username);

    UserInfoModel queryByStudentId(@Param("studentId") String StudentId);

    Integer queryTotalByRoleId(@Param("roleId") Integer roleId);

    void save(@Param("record") UserInfoModel record);

    void updateByIdSelective(@Param("record") UserInfoModel record);

    void delete(@Param("ids") List<Integer> ids);
}
