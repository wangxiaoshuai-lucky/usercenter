package com.kelab.usercenter.dal.repo;

import com.kelab.info.base.query.PageQuery;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.dal.domain.UserRankDomain;
import com.kelab.usercenter.result.AcSubmitResult;

import java.util.List;

public interface UserRankRepo {

    List<UserRankDomain> queryPageByTimeType(TimeType timeType, PageQuery pageQuery);

    /**
     * 更新一个人的各个榜单提交数量
     */
    void update(Integer userId, boolean ac);

    /**
     * 如果没有榜单则初始化
     */
    void checkAndInit(Integer userId);

    Integer total(TimeType timeType);

    void delete(TimeType timeType);

    AcSubmitResult queryCount(TimeType timeType);
}
