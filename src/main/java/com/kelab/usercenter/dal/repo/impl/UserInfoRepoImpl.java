package com.kelab.usercenter.dal.repo.impl;

import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.query.usercenter.UserQuery;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.convert.UserInfoConvert;
import com.kelab.usercenter.dal.dao.UserInfoMapper;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import com.kelab.usercenter.dal.repo.UserSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class UserInfoRepoImpl implements UserInfoRepo {

    private final UserInfoMapper userInfoMapper;

    private final UserSubmitInfoRepo userSubmitInfoRepo;

    private final RedisCache redisCache;

    @Autowired(required = false)
    public UserInfoRepoImpl(UserInfoMapper userInfoMapper,
                            UserSubmitInfoRepo userSubmitInfoRepo,
                            RedisCache redisCache) {
        this.userInfoMapper = userInfoMapper;
        this.userSubmitInfoRepo = userSubmitInfoRepo;
        this.redisCache = redisCache;
    }

    public List<UserInfoDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo) {
        List<UserInfoModel> userInfoModels = redisCache.cacheList(CacheBizName.USER_INFO, ids, UserInfoModel.class, missKeyList -> {
            List<UserInfoModel> dbModels = userInfoMapper.queryByIds(missKeyList);
            if (dbModels == null) {
                return null;
            }
            return dbModels.stream().collect(Collectors.toMap(UserInfoModel::getId, v -> v));
        });
        if (CollectionUtils.isEmpty(userInfoModels)) {
            return Collections.emptyList();
        }
        List<UserInfoDomain> domains = new ArrayList<>(userInfoModels.size());
        userInfoModels.forEach(item -> domains.add(UserInfoConvert.modelToDomain(item)));
        if (withSubmitInfo) {
            fillUserInfoSubmitInfo(domains);
        }
        return domains;
    }

    @Override
    @Verify(notNull = {"userQuery.page", "userQuery.rows"})
    public List<UserInfoDomain> queryPage(UserQuery userQuery, boolean withSubmitInfo) {
        List<UserInfoModel> models = userInfoMapper.queryPage(userQuery);
        List<UserInfoDomain> domains = new ArrayList<>(models.size());
        models.forEach(item -> domains.add(UserInfoConvert.modelToDomain(item)));
        if (withSubmitInfo) {
            fillUserInfoSubmitInfo(domains);
        }
        return domains;
    }

    @Override
    public Integer queryTotal(UserQuery userQuery) {
        return userInfoMapper.queryTotal(userQuery);
    }

    @Override
    public UserInfoDomain queryByUsername(String username, boolean withSubmitInfo) {
        UserInfoDomain domain = UserInfoConvert.modelToDomain(userInfoMapper.queryByUsername(username));
        if (domain != null && withSubmitInfo) {
            fillUserInfoSubmitInfo(domain);
        }
        return domain;
    }

    @Override
    public UserInfoDomain queryByStudentId(String studentId, boolean withSubmitInfo) {
        UserInfoDomain domain = UserInfoConvert.modelToDomain(userInfoMapper.queryByStudentId(studentId));
        if (domain != null && withSubmitInfo) {
            fillUserInfoSubmitInfo(domain);
        }
        return domain;
    }

    @Override
    public void save(UserInfoDomain userInfoDomain) {
        // 保存用户基本信息
        UserInfoModel userInfoModel = UserInfoConvert.domainToModel(userInfoDomain);
        userInfoMapper.save(userInfoModel);
        UserSubmitInfoDomain userSubmitInfoDomain = userInfoDomain.getSubmitInfo();
        // 回填插入的userId
        userInfoDomain.setId(userInfoModel.getId());
        userSubmitInfoDomain.setUserId(userInfoModel.getId());
        // 插入之后会id回填
        userSubmitInfoRepo.save(userSubmitInfoDomain);
    }

    @Override
    @Verify(notNull = "userInfoDomain.id")
    public void update(UserInfoDomain userInfoDomain) {
        UserInfoModel userInfoModel = UserInfoConvert.domainToModel(userInfoDomain);
        userInfoMapper.updateByIdSelective(userInfoModel);
        // 删除缓存
        redisCache.delete(CacheBizName.USER_INFO, userInfoDomain.getId());
        // 更新submitInfo
        UserSubmitInfoDomain userSubmitInfoDomain = userInfoDomain.getSubmitInfo();
        if (userSubmitInfoDomain != null) {
            userSubmitInfoRepo.update(userSubmitInfoDomain);
        }
    }

    @Override
    public void delete(List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        // 删除基本数据
        userInfoMapper.delete(ids);
        redisCache.deleteList(CacheBizName.USER_INFO, ids);
        userSubmitInfoRepo.delete(ids);
    }

    private void fillUserInfoSubmitInfo(List<UserInfoDomain> domains) {
        if (domains == null || domains.size() == 0) {
            return;
        }
        List<Integer> userIds = domains.stream().map(UserInfoDomain::getId).collect(Collectors.toList());
        List<UserSubmitInfoDomain> submitInfoDomains = userSubmitInfoRepo.queryByUserIds(userIds);
        Map<Integer, UserSubmitInfoDomain> submitInfoDomainMap =
                submitInfoDomains.stream().collect(Collectors.toMap(UserSubmitInfoDomain::getUserId, obj -> obj));
        domains.forEach(item -> item.setSubmitInfo(submitInfoDomainMap.get(item.getId())));
    }

    private void fillUserInfoSubmitInfo(UserInfoDomain domain) {
        if (domain == null) {
            return;
        }
        List<UserSubmitInfoDomain> userSubmitInfoDomains = userSubmitInfoRepo.queryByUserIds(Collections.singletonList(domain.getId()));
        domain.setSubmitInfo(userSubmitInfoDomains.get(0));
    }
}
