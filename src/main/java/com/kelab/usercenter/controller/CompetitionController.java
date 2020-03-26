package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.google.common.base.Preconditions;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.CompetitionInfo;
import com.kelab.info.usercenter.info.CompetitionStudentInfo;
import com.kelab.info.usercenter.info.CompetitionTeamInfo;
import com.kelab.info.usercenter.info.CompetitionTeamStudentInfo;
import com.kelab.info.usercenter.query.CompetitionTeamQuery;
import com.kelab.usercenter.convert.CompetitionConvert;
import com.kelab.usercenter.serivce.CompetitionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CompetitionController {

    private CompetitionService competitionService;

    public CompetitionController(CompetitionService competitionService) {
        this.competitionService = competitionService;
    }

    /**
     * 查询竞赛
     */
    @GetMapping("/competition.do")
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public JsonAndModel queryCompetitionPage(Context context, BaseQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(competitionService.queryCompetitionPage(context, query))
                .build();
    }

    /**
     * 更新竞赛
     */
    @PutMapping("/competition.do")
    @Verify(notNull = "record.id")
    public JsonAndModel updateCompetition(Context context, @RequestBody CompetitionInfo record) {
        competitionService.updateCompetition(context, CompetitionConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 添加竞赛
     */
    @PostMapping("/competition.do")
    @Verify(notNull = {"record.title", "record.description", "record.active"})
    public JsonAndModel saveCompetition(Context context, @RequestBody CompetitionInfo record) {
        competitionService.saveCompetition(context, CompetitionConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 删除竞赛
     */
    @DeleteMapping("/competition.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel deleteCompetition(Context context, @RequestParam("ids") List<Integer> ids) {
        competitionService.deleteCompetition(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 查询竞赛队伍
     */
    @GetMapping("/competition/team.do")
    @Verify(
            notNull = "query.competitionId",
            numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"}
    )
    public JsonAndModel queryCompetitionTeamPage(Context context, CompetitionTeamQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(competitionService.queryTeamPage(context, query))
                .build();
    }

    /**
     * 添加团队
     */
    @PostMapping("/competition/team.do")
    @Verify(
            notNull = {"record.competitionTeam.competitionId", "record.competitionTeam.teamName"},
            sizeLimit = "record.members [2, 3]"
    )
    public JsonAndModel saveTeam(Context context, @RequestBody CompetitionTeamStudentInfo record) {
        checkStudentInfoList(record.getMembers());
        return JsonAndModel.builder(competitionService.saveTeam(context, CompetitionConvert.infoToDomain(record))).build();
    }

    /**
     * 审核团队
     */
    @PutMapping("/competition/team.do")
    @Verify(notNull = {"teamInfo.id", "teamInfo.status"})
    public JsonAndModel updateTeam(Context context, @RequestBody CompetitionTeamInfo teamInfo) {
        competitionService.updateTeam(context, CompetitionConvert.infoToDomain(teamInfo));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 导出比赛团队信息
     */
    @GetMapping("/competition/export.do")
    @Verify(notNull = "*")
    public Object downloadTeamMessage(Context context, @RequestParam Integer competitionId) {
        return competitionService.downloadTeamMessage(context, competitionId);
    }

    /**
     * 检查队员参数
     */
    private void checkStudentInfoList(List<CompetitionStudentInfo> records) {
        // 队长手机号不为空
        Preconditions.checkNotNull(records.get(0).getPhone());
        for (CompetitionStudentInfo studentInfo : records) {
            Preconditions.checkNotNull(studentInfo.getStuName());
            Preconditions.checkNotNull(studentInfo.getStuNum());
            Preconditions.checkNotNull(studentInfo.getCollege());
            Preconditions.checkNotNull(studentInfo.getStuClass());
            Preconditions.checkNotNull(studentInfo.getSex());
            Preconditions.checkNotNull(studentInfo.getSize());
        }
    }
}
