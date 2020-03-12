package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.constant.enums.CacheConstant;
import com.kelab.usercenter.convert.SiteSettingConvert;
import com.kelab.usercenter.dal.dao.SiteSettingMapper;
import com.kelab.usercenter.dal.domain.SiteSettingDomain;
import com.kelab.usercenter.dal.model.SiteSettingModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.SiteSettingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SiteSettingRepoImpl implements SiteSettingRepo {

    private final SiteSettingMapper siteSettingMapper;

    private final RedisCache redisCache;

    @Autowired(required = false)
    public SiteSettingRepoImpl(SiteSettingMapper siteSettingMapper, RedisCache redisCache) {
        this.siteSettingMapper = siteSettingMapper;
        this.redisCache = redisCache;
    }

    @Override
    public List<SiteSettingDomain> queryByIds(List<Integer> ids) {
        List<SiteSettingModel> siteSettings = redisCache.cacheList(CacheConstant.SITE_SETTING, ids, SiteSettingModel.class, missKeyList -> {
            List<SiteSettingModel> models = siteSettingMapper.queryByIds(missKeyList);
            if (models == null) {
                return null;
            }
            return models.stream().collect(Collectors.toMap(SiteSettingModel::getId, v -> v));
        });
        List<SiteSettingDomain> domains = new ArrayList<>(siteSettings.size());
        siteSettings.forEach(item -> domains.add(SiteSettingConvert.modelToDomain(item)));
        return domains;
    }
}
