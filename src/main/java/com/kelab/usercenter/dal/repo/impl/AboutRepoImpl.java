package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.usercenter.info.ChangeOrderInfo;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.AboutConvert;
import com.kelab.usercenter.dal.dao.AboutMapper;
import com.kelab.usercenter.dal.domain.AboutDomain;
import com.kelab.usercenter.dal.model.AboutModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.AboutRepo;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AboutRepoImpl implements AboutRepo {


    private RedisCache redisCache;

    private AboutMapper aboutMapper;

    @Autowired(required = false)
    public AboutRepoImpl(RedisCache redisCache,
                         AboutMapper aboutMapper) {
        this.redisCache = redisCache;
        this.aboutMapper = aboutMapper;
    }

    /**
     * page::rows
     */
    private String buildCacheKey(BaseQuery query) {
        return query.getPage() + "::" + query.getRows();
    }

    @Override
    public List<AboutDomain> queryPage(BaseQuery query) {
        String cacheObjs = redisCache.cacheOne(
                CacheBizName.ABOUT_PAGE, buildCacheKey(query),
                String.class, missKey -> JSON.toJSONString(aboutMapper.queryPage(query)));
        List<AboutModel> models = JSON.parseArray(cacheObjs, AboutModel.class);
        return convertToDomain(models);
    }

    @Override
    public Integer queryTotal() {
        return aboutMapper.queryTotal();
    }

    @Override
    public List<AboutDomain> queryByIds(List<Integer> ids) {
        List<AboutModel> models = redisCache.cacheList(CacheBizName.ABOUT, ids,
                AboutModel.class, missKeyList -> {
                    List<AboutModel> dbModels = aboutMapper.queryByIds(missKeyList);
                    if (CollectionUtils.isEmpty(dbModels)) {
                        return null;
                    }
                    return dbModels.stream().collect(Collectors.toMap(AboutModel::getId, obj -> obj, (v1, v2) -> v2));
                });
        return convertToDomain(models);
    }

    @Override
    public void update(AboutDomain domain) {
        aboutMapper.update(AboutConvert.domainToModel(domain));
        redisCache.delete(CacheBizName.ABOUT, domain.getId());
        cleanPageCache();
    }

    @Override
    public void save(AboutDomain domain) {
        aboutMapper.save(AboutConvert.domainToModel(domain));
        cleanPageCache();
    }

    @Override
    public void delete(List<Integer> ids) {
        aboutMapper.delete(ids);
        redisCache.deleteList(CacheBizName.ABOUT, ids);
        cleanPageCache();
    }

    @Override
    public void changeAboutOrder(ChangeOrderInfo record) {
        List<AboutDomain> records = this.queryByIds(Collections.singletonList(record.getId()));
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        if (BooleanUtils.isTrue(record.getUp())) {
            AboutDomain nowRecord = records.get(0);
            AboutModel preRecord = aboutMapper.queryPreRecord(nowRecord.getAboutOrder());
            if (preRecord != null) {
                aboutMapper.changeOrder(nowRecord.getId(), preRecord.getAboutOrder(),
                        preRecord.getId(), nowRecord.getAboutOrder());
                redisCache.deleteList(CacheBizName.ABOUT, Arrays.asList(preRecord.getId(), nowRecord.getId()));
                this.cleanPageCache();
            }
        } else if (BooleanUtils.isTrue(record.getDown())) {
            AboutDomain nowRecord = records.get(0);
            AboutModel nextRecord = aboutMapper.queryNextRecord(nowRecord.getAboutOrder());
            if (nextRecord != null) {
                aboutMapper.changeOrder(nowRecord.getId(), nextRecord.getAboutOrder(),
                        nextRecord.getId(), nowRecord.getAboutOrder());
                redisCache.deleteList(CacheBizName.ABOUT, Arrays.asList(nextRecord.getId(), nowRecord.getId()));
                this.cleanPageCache();
            }
        }
    }

    /**
     * 删除分页缓存
     */
    private void cleanPageCache() {
        redisCache.deleteByPre(CacheBizName.ABOUT_PAGE, "");
    }

    private List<AboutDomain> convertToDomain(List<AboutModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(AboutConvert::modelToDomain).collect(Collectors.toList());
    }
}
