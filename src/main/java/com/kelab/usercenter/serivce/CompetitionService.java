package com.kelab.usercenter.serivce;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.CompetitionInfo;
import com.kelab.info.usercenter.info.CompetitionTeamInfo;
import com.kelab.info.usercenter.info.CompetitionTeamStudentInfo;
import com.kelab.info.usercenter.query.CompetitionTeamQuery;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamStudentDomain;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CompetitionService {

    /**
     * 查询竞赛
     */
    PaginationResult<CompetitionInfo> queryCompetitionPage(Context context, BaseQuery query);

    /**
     * 添加竞赛
     */
    void saveCompetition(Context context, CompetitionDomain record);

    /**
     * 更新竞赛
     */
    void updateCompetition(Context context, CompetitionDomain record);

    /**
     * 删除竞赛
     */
    void deleteCompetition(Context context, List<Integer> ids);

    /**
     * 获取团队列表
     */
    PaginationResult<CompetitionTeamStudentInfo> queryTeamPage(Context context, CompetitionTeamQuery query);

    /**
     * 报名团队
     */
    String saveTeam(Context context, CompetitionTeamStudentDomain record);

    /**
     * 审核团队
     * 由于本人能力不够，现在只能修改状态
     */
    void updateTeam(Context context, CompetitionTeamDomain record);

    /**
     * 下载比赛队伍信息
     */
    ResponseEntity<byte[]> downloadTeamMessage(Context context, int competitionId);
}
