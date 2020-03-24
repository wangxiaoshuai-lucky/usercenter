package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.CompetitionConvert;
import com.kelab.usercenter.dal.dao.CompetitionMapper;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.model.CompetitionModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.CompetitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CompetitionRepoImpl implements CompetitionRepo {

    private RedisCache redisCache;

    private CompetitionMapper competitionMapper;

    @Autowired(required = false)
    public CompetitionRepoImpl(RedisCache redisCache,
                               CompetitionMapper competitionMapper) {
        this.redisCache = redisCache;
        this.competitionMapper = competitionMapper;
    }

    /**
     * page::rows
     */
    private String buildCacheKey(BaseQuery query) {
        return query.getPage() + "::" + query.getRows();
    }

    @Override
    public List<CompetitionDomain> queryPage(BaseQuery query) {
        String cacheObjs = redisCache.cacheOne(
                CacheBizName.COMPETITION_PAGE, buildCacheKey(query),
                String.class, missKey -> JSON.toJSONString(competitionMapper.queryPage(query)));
        List<CompetitionModel> models = JSON.parseArray(cacheObjs, CompetitionModel.class);
        return convertToDomain(models);
    }

    @Override
    public Integer queryTotal() {
        return competitionMapper.queryTotal();
    }

    @Override
    public List<CompetitionDomain> queryByIds(List<Integer> ids) {
        List<CompetitionModel> models = redisCache.cacheList(CacheBizName.COMPETITION, ids,
                CompetitionModel.class, missKeyList -> {
                    List<CompetitionModel> dbModels = competitionMapper.queryByIds(missKeyList);
                    if (CollectionUtils.isEmpty(dbModels)) {
                        return null;
                    }
                    return dbModels.stream().collect(Collectors.toMap(CompetitionModel::getId, obj -> obj, (v1, v2) -> v2));
                });
        return convertToDomain(models);
    }

    @Override
    public void update(CompetitionDomain domain) {
        competitionMapper.update(CompetitionConvert.domainToModel(domain));
        redisCache.delete(CacheBizName.COMPETITION, domain.getId());
        cleanPageCache();
    }

    @Override
    public void save(CompetitionDomain domain) {
        competitionMapper.save(CompetitionConvert.domainToModel(domain));
        cleanPageCache();
    }

    @Override
    public void delete(List<Integer> ids) {
        competitionMapper.delete(ids);
        redisCache.deleteList(CacheBizName.COMPETITION, ids);
        cleanPageCache();
    }

    /**
     * 删除分页缓存
     */
    private void cleanPageCache() {
        redisCache.deleteByPre(CacheBizName.COMPETITION_PAGE, "");
    }

    private List<CompetitionDomain> convertToDomain(List<CompetitionModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(CompetitionConvert::modelToDomain).collect(Collectors.toList());
    }
}
