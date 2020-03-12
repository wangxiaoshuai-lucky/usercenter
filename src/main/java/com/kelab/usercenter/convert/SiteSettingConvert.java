package com.kelab.usercenter.convert;

import com.kelab.usercenter.dal.domain.SiteSettingDomain;
import com.kelab.usercenter.dal.model.SiteSettingModel;
import org.springframework.beans.BeanUtils;

public class SiteSettingConvert {

    /**
     * model to domain
     */
    public static SiteSettingDomain modelToDomain(SiteSettingModel model) {
        if (model == null) {
            return null;
        }
        SiteSettingDomain domain = new SiteSettingDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }
}
