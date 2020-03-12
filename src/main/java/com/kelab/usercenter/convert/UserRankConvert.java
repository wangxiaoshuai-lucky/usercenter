package com.kelab.usercenter.convert;

import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.dal.domain.UserRankDomain;
import com.kelab.usercenter.dal.model.UserRankModel;

public class UserRankConvert {

    /**
     * model to domain
     */
    public static UserRankDomain modelToDomain(UserRankModel model) {
        if (model == null) {
            return null;
        }
        UserRankDomain domain = new UserRankDomain();
        domain.setAcNum(model.getAcNum());
        domain.setSubmitNum(model.getSubmitNum());
        domain.setId(model.getId());
        domain.setUserId(model.getUserId());
        try {
            domain.setTimeType(TimeType.valueOf(model.getTimeType()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return domain;
    }
}
