package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.CompetitionStudentModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompetitionStudentMapper {

    /**
     * 通过队伍ids查询队员
     */
    List<CompetitionStudentModel> queryByTeamIds(@Param("teamIds") List<Integer> teamIds);

    /**
     * 批量插入队员
     */
    void insertList(@Param("records") List<CompetitionStudentModel> records);
}
