package com.kelab.usercenter.dal.dao;

import com.kelab.info.context.Context;
import com.kelab.info.usercenter.query.CompetitionTeamQuery;
import com.kelab.usercenter.dal.domain.CompetitionTeamDomain;
import com.kelab.usercenter.dal.model.CompetitionTeamModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompetitionTeamMapper {

    /**
     * 通过比赛id分页查询团队
     */
    List<CompetitionTeamModel> queryPage(@Param("query") CompetitionTeamQuery query);

    /**
     * 查找重复名字
     */
    Integer queryTotalByName(@Param("competitionId") Integer competitionId, @Param("teamName") String teamName);

    /**
     * 查询团队个数
     */
    Integer queryTeamTotal(@Param("query") CompetitionTeamQuery query);


    /**
     * 新加队伍
     */
    void save(@Param("record") CompetitionTeamModel record);

    /**
     * 审核团队
     */
    void update(@Param("record") CompetitionTeamModel record);
}
