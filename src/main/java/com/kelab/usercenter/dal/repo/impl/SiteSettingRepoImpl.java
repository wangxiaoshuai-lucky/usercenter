package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.SiteSettingConvert;
import com.kelab.usercenter.dal.dao.SiteSettingMapper;
import com.kelab.usercenter.dal.domain.SiteSettingDomain;
import com.kelab.usercenter.dal.model.SiteSettingModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.SiteSettingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
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
        List<SiteSettingModel> siteSettings = redisCache.cacheList(CacheBizName.SITE_SETTING, ids, SiteSettingModel.class, missKeyList -> {
            List<SiteSettingModel> models = siteSettingMapper.queryByIds(missKeyList);
            if (models == null) {
                return null;
            }
            return models.stream().collect(Collectors.toMap(SiteSettingModel::getId, v -> v, (v1, v2) -> v2));
        });
        if (CollectionUtils.isEmpty(siteSettings)) {
            return Collections.emptyList();
        }
        return siteSettings.stream().map(SiteSettingConvert::modelToDomain).collect(Collectors.toList());
    }
}
