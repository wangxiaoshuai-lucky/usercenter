package com.kelab.usercenter.dal.repo.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.usercenter.query.CompetitionTeamQuery;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.CompetitionConvert;
import com.kelab.usercenter.dal.dao.CompetitionMapper;
import com.kelab.usercenter.dal.dao.CompetitionStudentMapper;
import com.kelab.usercenter.dal.dao.CompetitionTeamMapper;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.domain.CompetitionStudentDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamStudentDomain;
import com.kelab.usercenter.dal.model.CompetitionModel;
import com.kelab.usercenter.dal.model.CompetitionStudentModel;
import com.kelab.usercenter.dal.model.CompetitionTeamModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.CompetitionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CompetitionRepoImpl implements CompetitionRepo {

    private RedisCache redisCache;

    private CompetitionMapper competitionMapper;

    private CompetitionTeamMapper competitionTeamMapper;

    private CompetitionStudentMapper competitionStudentMapper;

    @Autowired(required = false)
    public CompetitionRepoImpl(RedisCache redisCache,
                               CompetitionMapper competitionMapper,
                               CompetitionTeamMapper competitionTeamMapper,
                               CompetitionStudentMapper competitionStudentMapper) {
        this.redisCache = redisCache;
        this.competitionMapper = competitionMapper;
        this.competitionTeamMapper = competitionTeamMapper;
        this.competitionStudentMapper = competitionStudentMapper;
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
        return convertToCompetitionDomain(models);
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
        return convertToCompetitionDomain(models);
    }

    @Override
    public void updateTeam(CompetitionDomain domain) {
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

    @Override
    public List<CompetitionTeamStudentDomain> queryTeamPage(CompetitionTeamQuery query) {
        List<CompetitionTeamDomain> teams = convertToTeamDomain(competitionTeamMapper.queryPage(query));
        List<Integer> teamIds = teams.stream().map(CompetitionTeamDomain::getId).collect(Collectors.toList());
        Map<Integer, List<CompetitionStudentDomain>> studentMap = this.queryStudentsByTeamIds(teamIds);
        return teams.stream().map(item -> new CompetitionTeamStudentDomain(item, studentMap.get(item.getId()))).collect(Collectors.toList());
    }

    @Override
    public Integer queryTeamTotal(CompetitionTeamQuery query) {
        return competitionTeamMapper.queryTeamTotal(query);
    }

    @Override
    public void saveTeamInfo(CompetitionTeamStudentDomain record) {
        CompetitionTeamDomain team = record.getTeamInfoDomain();
        CompetitionTeamModel model = CompetitionConvert.domainToModel(team);
        competitionTeamMapper.save(model);
        team.setId(model.getId());
        record.getMembers().forEach(item -> item.setTeamId(model.getId()));
        List<CompetitionStudentModel> studentModels = record.getMembers().stream()
                .map(CompetitionConvert::domainToModel).collect(Collectors.toList());
        competitionStudentMapper.insertList(studentModels);
    }

    @Override
    public Integer queryTotalByName(Integer competitionId, String teamName) {
        return competitionTeamMapper.queryTotalByName(competitionId, teamName);
    }

    @Override
    public void updateTeam(CompetitionTeamDomain record) {
        competitionTeamMapper.update(CompetitionConvert.domainToModel(record));
    }

    private Map<Integer, List<CompetitionStudentDomain>> queryStudentsByTeamIds(List<Integer> teamIds) {
        /*
         * cacheObj: List< JSON(List<CompetitionStudentModel>) >
         */
        List<String> cacheObj = redisCache.cacheList(CacheBizName.COMPETITION_STUDENT, teamIds,
                String.class, missKeyList -> {
                    List<CompetitionStudentModel> dbModel = competitionStudentMapper.queryByTeamIds(missKeyList);
                    if (CollectionUtils.isEmpty(dbModel)) {
                        return null;
                    }
                    Map<Integer, List<CompetitionStudentModel>> dbMap = dbModel.stream().collect(
                            Collectors.groupingBy(CompetitionStudentModel::getTeamId, Collectors.toList()));
                    Map<Integer, String> dbJsonMap = new HashMap<>();
                    dbMap.forEach((k, v) -> dbJsonMap.put(k, JSON.toJSONString(v)));
                    return dbJsonMap;
                });
        return cacheObj.stream().map(item -> convertToStuDomain(JSON.parseArray(item, CompetitionStudentModel.class))) // List<List<CompetitionStudentDomain>>
                .collect(Collectors.toMap(item -> item.get(0).getTeamId(), list -> list, (v1, v2) -> v2));// to map
    }

    /**
     * 删除分页缓存
     */
    private void cleanPageCache() {
        redisCache.deleteByPre(CacheBizName.COMPETITION_PAGE, "");
    }

    private List<CompetitionDomain> convertToCompetitionDomain(List<CompetitionModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(CompetitionConvert::modelToDomain).collect(Collectors.toList());
    }

    private List<CompetitionTeamDomain> convertToTeamDomain(List<CompetitionTeamModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(CompetitionConvert::modelToDomain).collect(Collectors.toList());
    }

    private List<CompetitionStudentDomain> convertToStuDomain(List<CompetitionStudentModel> models) {
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        return models.stream().map(CompetitionConvert::modelToDomain).collect(Collectors.toList());
    }
}
