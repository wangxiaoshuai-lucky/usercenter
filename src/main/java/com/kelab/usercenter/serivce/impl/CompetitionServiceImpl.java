package com.kelab.usercenter.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.CompetitionInfo;
import com.kelab.info.usercenter.info.CompetitionTeamStudentInfo;
import com.kelab.info.usercenter.query.CompetitionTeamQuery;
import com.kelab.usercenter.constant.enums.CompetitionTeamStatus;
import com.kelab.usercenter.convert.CompetitionConvert;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamStudentDomain;
import com.kelab.usercenter.dal.repo.CompetitionRepo;
import com.kelab.usercenter.serivce.CompetitionService;
import com.kelab.usercenter.support.ContextLogger;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    private ContextLogger contextLogger;

    private CompetitionRepo competitionRepo;

    public CompetitionServiceImpl(ContextLogger contextLogger,
                                  CompetitionRepo competitionRepo) {
        this.contextLogger = contextLogger;
        this.competitionRepo = competitionRepo;
    }

    @Override
    public PaginationResult<CompetitionInfo> queryCompetitionPage(Context context, BaseQuery query) {
        PaginationResult<CompetitionInfo> result = new PaginationResult<>();
        List<Integer> totalIds = CommonService.totalIds(query);
        if (!CollectionUtils.isEmpty(totalIds)) {
            List<CompetitionDomain> domains = competitionRepo.queryByIds(totalIds);
            result.setPagingList(competitionDomainToInfo(domains));
            result.setTotal(domains.size());
        } else {
            List<CompetitionDomain> domains = competitionRepo.queryPage(query);
            result.setPagingList(competitionDomainToInfo(domains));
            result.setTotal(competitionRepo.queryTotal());
        }
        return result;
    }

    @Override
    public void saveCompetition(Context context, CompetitionDomain record) {
        competitionRepo.save(record);
    }

    @Override
    public void updateCompetition(Context context, CompetitionDomain record) {
        competitionRepo.updateTeam(record);
    }

    @Override
    public void deleteCompetition(Context context, List<Integer> ids) {
        List<CompetitionDomain> old = competitionRepo.queryByIds(ids);
        competitionRepo.delete(ids);
        contextLogger.info(context, "删除竞赛：%s", JSON.toJSONString(old));
    }

    @Override
    public PaginationResult<CompetitionTeamStudentInfo> queryTeamPage(Context context, CompetitionTeamQuery query) {
        PaginationResult<CompetitionTeamStudentInfo> result = new PaginationResult<>();
        result.setPagingList(teamStudentDomainToInfo(competitionRepo.queryTeamPage(query)));
        result.setTotal(competitionRepo.queryTeamTotal(query));
        return result;
    }

    @Override
    public String saveTeam(Context context, CompetitionTeamStudentDomain record) {
        CompetitionTeamDomain team = record.getTeamInfoDomain();
        List<CompetitionDomain> competitions = competitionRepo.queryByIds(Collections.singletonList(team.getCompetitionId()));
        if (CollectionUtils.isEmpty(competitions) || BooleanUtils.isNotTrue(competitions.get(0).getActive())) {
            return StatusMsgConstant.COMPETITION_NOT_EXIST_OR_CLOSE;
        }
        if (competitionRepo.queryTotalByName(team.getCompetitionId(), team.getTeamName()) > 0) {
            return StatusMsgConstant.TEAM_IS_EXISTED;
        }
        // 默认为等待
        team.setStatus(CompetitionTeamStatus.PENDING);
        competitionRepo.saveTeamInfo(record);
        return StatusMsgConstant.SUCCESS;
    }

    @Override
    public void updateTeam(Context context, CompetitionTeamDomain record) {
        competitionRepo.updateTeam(record);
    }

    private List<CompetitionInfo> competitionDomainToInfo(List<CompetitionDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(CompetitionConvert::domainToInfo).collect(Collectors.toList());
    }

    private List<CompetitionTeamStudentInfo> teamStudentDomainToInfo(List<CompetitionTeamStudentDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(CompetitionConvert::domainToInfo).collect(Collectors.toList());
    }
}
