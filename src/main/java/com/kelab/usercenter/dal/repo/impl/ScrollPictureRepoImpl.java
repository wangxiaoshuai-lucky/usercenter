package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.ScrollPictureConvert;
import com.kelab.usercenter.dal.dao.ScrollPictureMapper;
import com.kelab.usercenter.dal.domain.ScrollPictureDomain;
import com.kelab.usercenter.dal.model.ScrollPictureModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.ScrollPictureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@SuppressWarnings("unchecked")
public class ScrollPictureRepoImpl implements ScrollPictureRepo {

    private ScrollPictureMapper scrollPictureMapper;

    private RedisCache redisCache;

    @Autowired(required = false)
    public ScrollPictureRepoImpl(ScrollPictureMapper scrollPictureMapper,
                                 RedisCache redisCache) {
        this.scrollPictureMapper = scrollPictureMapper;
        this.redisCache = redisCache;
    }

    /**
     * page::rows::active
     */
    private String buildCacheKey(ScrollPictureQuery query) {
        StringBuilder sb = new StringBuilder();
        sb.append(query.getPage());
        sb.append("::");
        sb.append(query.getRows());
        if (query.getActive() != null) {
            sb.append("::");
            sb.append(query.getActive());
        }
        return sb.toString();
    }

    @Override
    public List<ScrollPictureDomain> queryPage(ScrollPictureQuery query) {
        String cacheObjs = redisCache.cacheOne(
                CacheBizName.SCROLL_PICTURE_PAGE, buildCacheKey(query),
                String.class, missKey -> JSON.toJSONString(scrollPictureMapper.queryPage(query)));
        List<ScrollPictureModel> models = JSON.parseArray(cacheObjs, ScrollPictureModel.class);
        return convertToDomain(models);
    }

    @Override
    public Integer queryTotal(ScrollPictureQuery query) {
        return scrollPictureMapper.queryTotal(query);
    }

    @Override
    public List<ScrollPictureDomain> queryByIds(List<Integer> ids) {
        List<ScrollPictureModel> models = redisCache.cacheList(
                CacheBizName.SCROLL_PICTURE, ids,
                ScrollPictureModel.class, missKeys -> {
                    List<ScrollPictureModel> dbModels = scrollPictureMapper.queryByIds(missKeys);
                    if (CollectionUtils.isEmpty(dbModels)) {
                        return null;
                    }
                    return dbModels.stream().collect(Collectors.toMap(ScrollPictureModel::getId, obj -> obj, (v1, v2) -> v2));
                });
        return convertToDomain(models);
    }

    @Override
    public void update(ScrollPictureDomain domain) {
        scrollPictureMapper.update(ScrollPictureConvert.domainToModel(domain));
        redisCache.delete(CacheBizName.SCROLL_PICTURE, domain.getId());
        cleanPageCache();
    }

    @Override
    public void save(ScrollPictureDomain domain) {
        scrollPictureMapper.save(ScrollPictureConvert.domainToModel(domain));
        cleanPageCache();
    }

    @Override
    public void delete(List<Integer> ids) {
        scrollPictureMapper.delete(ids);
        redisCache.delete(CacheBizName.SCROLL_PICTURE, ids);
        cleanPageCache();
    }

    /**
     * 删除分页缓存
     */
    private void cleanPageCache() {
        redisCache.deleteByPre(CacheBizName.SCROLL_PICTURE_PAGE, "");
    }

    private List<ScrollPictureDomain> convertToDomain(List<ScrollPictureModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(ScrollPictureConvert::modelToDomain).collect(Collectors.toList());
    }
}
