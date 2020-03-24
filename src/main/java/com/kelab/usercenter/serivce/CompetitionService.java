package com.kelab.usercenter.serivce;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.CompetitionInfo;
import com.kelab.usercenter.dal.domain.CompetitionDomain;

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
}
