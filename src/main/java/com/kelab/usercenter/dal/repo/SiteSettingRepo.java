package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.domain.SiteSettingDomain;

import java.util.List;

public interface SiteSettingRepo {

    List<SiteSettingDomain> queryByIds(List<Integer> ids);
}
