package com.kelab.usercenter.dal.repo;

import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.usercenter.query.CompetitionTeamQuery;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamStudentDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompetitionRepo {

    /**
     * 分页查询
     * 考虑到请求频繁,走缓存,同时在删除和更新的时候删除cache
     */
    List<CompetitionDomain> queryPage(BaseQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal();

    /**
     * 走缓存
     */
    List<CompetitionDomain> queryByIds(List<Integer> ids);

    /**
     * 更新
     */
    void updateTeam(CompetitionDomain domain);

    /**
     * 添加
     */
    void save(CompetitionDomain domain);

    /**
     * 删除
     */
    void delete(List<Integer> ids);

    /**
     * 分页查询竞赛团队
     */
    List<CompetitionTeamStudentDomain> queryTeamPage(CompetitionTeamQuery query);

    /**
     * 查询团队个数
     */
    Integer queryTeamTotal(CompetitionTeamQuery query);

    /**
     * 保存队伍信息
     */
    void saveTeamInfo(CompetitionTeamStudentDomain record);

    /**
     * 查重
     */
    Integer queryTotalByName(Integer competitionId, String teamName);

    /**
     * 审核团队
     */
    void updateTeam(CompetitionTeamDomain record);
}
