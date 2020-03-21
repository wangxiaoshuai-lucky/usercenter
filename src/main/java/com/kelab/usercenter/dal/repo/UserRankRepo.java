package com.kelab.usercenter.dal.repo;

import com.kelab.info.base.query.PageQuery;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.dal.domain.UserRankDomain;
import com.kelab.usercenter.result.AcSubmitResult;

import java.util.List;

public interface UserRankRepo {

    List<UserRankDomain> queryPageByTimeType(TimeType timeType, PageQuery pageQuery);

    Integer total(TimeType timeType);

    void delete(TimeType timeType);

    AcSubmitResult queryCount(TimeType timeType);
}
