package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.usercenter.query.NewsRollQuery;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.NewsConvert;
import com.kelab.usercenter.convert.NewsRollConvert;
import com.kelab.usercenter.dal.dao.NewsRollMapper;
import com.kelab.usercenter.dal.domain.NewsRollDomain;
import com.kelab.usercenter.dal.model.NewsModel;
import com.kelab.usercenter.dal.model.NewsRollModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.NewsRollRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class NewsRollRepoImpl implements NewsRollRepo {

    private NewsRollMapper newsRollMapper;

    private RedisCache redisCache;

    @Autowired(required = false)
    public NewsRollRepoImpl(NewsRollMapper newsRollMapper,
                            RedisCache redisCache) {
        this.newsRollMapper = newsRollMapper;
        this.redisCache = redisCache;
    }

    /**
     * page::rows::active
     */
    private String buildCacheKey(NewsRollQuery query) {
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
    public List<NewsRollDomain> queryPage(NewsRollQuery query) {
        String cacheObjs = redisCache.cacheOne(
                CacheBizName.NEWS_ROLL_PAGE, buildCacheKey(query),
                String.class, missKey -> JSON.toJSONString(newsRollMapper.queryPage(query)));
        List<NewsRollModel> models = JSON.parseArray(cacheObjs, NewsRollModel.class);
        return convertToDomain(models);
    }

    @Override
    public Integer queryTotal(NewsRollQuery query) {
        return newsRollMapper.queryTotal(query);
    }

    @Override
    public List<NewsRollDomain> queryByIds(List<Integer> ids) {
        List<NewsRollModel> models = redisCache.cacheList(CacheBizName.NEWS_ROLL, ids,
                NewsRollModel.class, missKeyList -> {
                    List<NewsRollModel> dbModels = newsRollMapper.queryByIds(missKeyList);
                    if (CollectionUtils.isEmpty(dbModels)) {
                        return null;
                    }
                    return dbModels.stream().collect(Collectors.toMap(NewsRollModel::getId, obj -> obj, (v1, v2) -> v2));
                });
        return convertToDomain(models);
    }

    @Override
    public void update(NewsRollDomain domain) {
        newsRollMapper.update(NewsRollConvert.domainToModel(domain));
        redisCache.delete(CacheBizName.NEWS_ROLL, domain.getId());
        cleanPageCache();
    }

    @Override
    public void save(NewsRollDomain domain) {
        newsRollMapper.save(NewsRollConvert.domainToModel(domain));
        cleanPageCache();
    }

    @Override
    public void delete(List<Integer> ids) {
        newsRollMapper.delete(ids);
        redisCache.deleteList(CacheBizName.NEWS_ROLL, ids);
        cleanPageCache();
    }

    /**
     * 删除分页缓存
     */
    private void cleanPageCache() {
        redisCache.deleteByPre(CacheBizName.NEWS_ROLL_PAGE, "");
    }

    private List<NewsRollDomain> convertToDomain(List<NewsRollModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(NewsRollConvert::modelToDomain).collect(Collectors.toList());
    }
}
