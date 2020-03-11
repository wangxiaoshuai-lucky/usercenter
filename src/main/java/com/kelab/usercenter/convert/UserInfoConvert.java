package com.kelab.usercenter.convert;

import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;
import org.springframework.beans.BeanUtils;

public class UserInfoConvert {

    /**
     * model to domain
     */
    public static UserInfoDomain modelToDomain(UserInfoModel model) {
        if (model == null) {
            return null;
        }
        UserInfoDomain domain = new UserInfoDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    /**
     * domain to model
     */
    public static UserInfoModel domainToModel(UserInfoDomain domain) {
        if (domain == null) {
            return null;
        }
        UserInfoModel model = new UserInfoModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    /**
     * info to domain
     */
    public static UserInfoDomain infoToDomain(UserInfo userInfo) {
        if (userInfo == null) {
            return null;
        }
        UserInfoDomain domain = new UserInfoDomain();
        BeanUtils.copyProperties(userInfo, domain);
        if (userInfo.getAcNum() != null && userInfo.getSubmitNum() != null) {
            UserSubmitInfoDomain submitInfoDomain = new UserSubmitInfoDomain();
            submitInfoDomain.setAcNum(userInfo.getAcNum());
            submitInfoDomain.setSubmitNum(userInfo.getSubmitNum());
            submitInfoDomain.setUserId(domain.getId());
            domain.setSubmitInfo(submitInfoDomain);
        }
        return domain;
    }

    /**
     * domain to info
     */
    public static UserInfo domainToInfo(UserInfoDomain userInfoDomain) {
        if (userInfoDomain == null) {
            return null;
        }
        UserInfo info = new UserInfo();
        BeanUtils.copyProperties(userInfoDomain, info);
        if (userInfoDomain.getSubmitInfo() != null) {
            UserSubmitInfoDomain submitInfoDomain = userInfoDomain.getSubmitInfo();
            info.setAcNum(submitInfoDomain.getAcNum());
            info.setSubmitNum(submitInfoDomain.getSubmitNum());
        }
        return info;
    }
}
