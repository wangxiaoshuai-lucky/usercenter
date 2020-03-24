package com.kelab.usercenter.convert;

import com.kelab.info.usercenter.info.AboutInfo;
import com.kelab.usercenter.dal.domain.AboutDomain;
import com.kelab.usercenter.dal.model.AboutModel;
import org.springframework.beans.BeanUtils;

public class AboutConvert {
    public static AboutDomain modelToDomain(AboutModel model) {
        if (model == null) {
            return null;
        }
        AboutDomain domain = new AboutDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static AboutModel domainToModel(AboutDomain domain) {
        if (domain == null) {
            return null;
        }
        AboutModel model = new AboutModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static AboutInfo domainToInfo(AboutDomain domain) {
        if (domain == null) {
            return null;
        }
        AboutInfo info = new AboutInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static AboutDomain infoToDomain(AboutInfo info) {
        if (info == null) {
            return null;
        }
        AboutDomain domain = new AboutDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }
}