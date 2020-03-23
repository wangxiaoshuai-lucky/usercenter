package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.usercenter.query.NewsQuery;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.NewsConvert;
import com.kelab.usercenter.convert.ScrollPictureConvert;
import com.kelab.usercenter.dal.dao.NewsMapper;
import com.kelab.usercenter.dal.domain.NewsDomain;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.model.NewsModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.NewsRepo;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class NewsRepoImpl implements NewsRepo {

    private NewsMapper newsMapper;

    private RedisCache redisCache;

    private UserInfoRepo userInfoRepo;

    @Autowired(required = false)
    public NewsRepoImpl(NewsMapper newsMapper,
                        RedisCache redisCache,
                        UserInfoRepo userInfoRepo) {
        this.newsMapper = newsMapper;
        this.redisCache = redisCache;
        this.userInfoRepo = userInfoRepo;
    }

    /**
     * page::rows::title:like
     * title 的命中率不高，只要是为了确定query的唯一性
     */
    private String buildCacheKey(NewsQuery query) {
        StringBuilder sb = new StringBuilder();
        sb.append(query.getPage());
        sb.append("::");
        sb.append(query.getRows());
        if (StringUtils.isNotBlank(query.getTitle())) {
            sb.append("::");
            sb.append(query.getTitle());
            sb.append("::");
            sb.append(query.isTitleLike());
        }
        return sb.toString();
    }

    @Override
    public List<NewsDomain> queryPage(NewsQuery query) {
        String cacheObjs = redisCache.cacheOne(
                CacheBizName.NEWS_PAGE, buildCacheKey(query),
                String.class, missKey -> JSON.toJSONString(newsMapper.queryPage(query)));
        List<NewsModel> models = JSON.parseArray(cacheObjs, NewsModel.class);
        return convertToDomain(models);
    }

    @Override
    public Integer queryTotal(NewsQuery query) {
        return newsMapper.queryTotal(query);
    }

    @Override
    public List<NewsDomain> queryByIds(List<Integer> ids) {
        List<NewsModel> models = redisCache.cacheList(CacheBizName.NEWS, ids,
                NewsModel.class, missKeyList -> {
                    List<NewsModel> dbModels = newsMapper.queryByIds(missKeyList);
                    if (CollectionUtils.isEmpty(dbModels)) {
                        return null;
                    }
                    return dbModels.stream().collect(Collectors.toMap(NewsModel::getId, obj -> obj, (v1, v2) -> v2));
                });
        return convertToDomain(models);
    }

    @Override
    public void update(NewsDomain domain) {
        newsMapper.update(NewsConvert.domainToModel(domain));
        redisCache.delete(CacheBizName.NEWS, domain.getId());
        cleanPageCache();
    }

    @Override
    public void save(NewsDomain domain) {
        newsMapper.save(NewsConvert.domainToModel(domain));
        cleanPageCache();
    }

    @Override
    public void delete(List<Integer> ids) {
        newsMapper.delete(ids);
        redisCache.deleteList(CacheBizName.NEWS, ids);
        cleanPageCache();
    }

    @Override
    public void addViewNumber(Integer id) {
        newsMapper.addViewNumber(id);
        redisCache.delete(CacheBizName.NEWS, id);
    }

    /**
     * 删除分页缓存
     */
    private void cleanPageCache() {
        redisCache.deleteByPre(CacheBizName.NEWS_PAGE, "");
    }

    private List<NewsDomain> convertToDomain(List<NewsModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<Integer> userIds = models.stream().map(NewsModel::getPublishUser).collect(Collectors.toList());
        Map<Integer, UserInfoDomain> userMap = userInfoRepo.queryByIds(userIds, false).stream().collect(Collectors.toMap(UserInfoDomain::getId, obj -> obj, (v1, v2) -> v2));
        List<NewsDomain> result = models.stream().map(NewsConvert::modelToDomain).collect(Collectors.toList());
        result.forEach(item -> item.setPublishUserInfo(userMap.get(item.getPublishUser())));
        return result;
    }
}
