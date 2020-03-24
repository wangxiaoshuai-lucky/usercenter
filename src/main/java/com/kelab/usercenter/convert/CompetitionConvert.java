package com.kelab.usercenter.convert;

import com.kelab.info.usercenter.info.CompetitionInfo;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.model.CompetitionModel;
import org.springframework.beans.BeanUtils;

public class CompetitionConvert {
    public static CompetitionDomain modelToDomain(CompetitionModel model) {
        if (model == null) {
            return null;
        }
        CompetitionDomain domain = new CompetitionDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static CompetitionModel domainToModel(CompetitionDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionModel model = new CompetitionModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static CompetitionInfo domainToInfo(CompetitionDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionInfo info = new CompetitionInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static CompetitionDomain infoToDomain(CompetitionInfo info) {
        if (info == null) {
            return null;
        }
        CompetitionDomain domain = new CompetitionDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }
}