package com.kelab.usercenter.convert;

import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;
import org.springframework.beans.BeanUtils;

public class UserInfoConvert {

    /**
     * model to domain
     * @param model model
     * @return domain
     */
    public static UserInfoDomain modelToDomain(UserInfoModel model) {
        UserInfoDomain domain = new UserInfoDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    /**
     * domain to model
     * @param domain domain
     * @return model
     */
    public static UserInfoModel modelToDomain(UserInfoDomain domain) {
        UserInfoModel model = new UserInfoModel();
        BeanUtils.copyProperties(model, domain);
        return model;
    }
}
