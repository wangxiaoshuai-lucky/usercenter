package com.kelab.usercenter.convert;

import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;
import com.kelab.usercenter.dal.model.UserSubmitInfoModel;
import org.springframework.beans.BeanUtils;

public class UserSubmitInfoConvert {

    /**
     * model to domain
     */
    public static UserSubmitInfoDomain modelToDomain(UserSubmitInfoModel model) {
        if (model == null) {
            return null;
        }
        UserSubmitInfoDomain domain = new UserSubmitInfoDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    /**
     * domain to model
     */
    public static UserSubmitInfoModel domainToModel(UserSubmitInfoDomain domain) {
        if (domain == null) {
            return null;
        }
        UserSubmitInfoModel model = new UserSubmitInfoModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }
}
