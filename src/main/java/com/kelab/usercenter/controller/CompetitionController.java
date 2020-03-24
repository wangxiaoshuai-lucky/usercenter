package com.kelab.usercenter.controller;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.CompetitionInfo;
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
}
