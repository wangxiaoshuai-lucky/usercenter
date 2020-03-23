package com.kelab.usercenter.convert;

import com.kelab.info.usercenter.info.NewsRollInfo;
import com.kelab.usercenter.dal.domain.NewsRollDomain;
import com.kelab.usercenter.dal.model.NewsRollModel;
import org.springframework.beans.BeanUtils;

public class NewsRollConvert {
    public static NewsRollDomain modelToDomain(NewsRollModel model) {
        if (model == null) {
            return null;
        }
        NewsRollDomain domain = new NewsRollDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static NewsRollModel domainToModel(NewsRollDomain domain) {
        if (domain == null) {
            return null;
        }
        NewsRollModel model = new NewsRollModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static NewsRollInfo domainToInfo(NewsRollDomain domain) {
        if (domain == null) {
            return null;
        }
        NewsRollInfo info = new NewsRollInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static NewsRollDomain infoToDomain(NewsRollInfo info) {
        if (info == null) {
            return null;
        }
        NewsRollDomain domain = new NewsRollDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }
}