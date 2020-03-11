package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.convert.UserInfoConvert;
import com.kelab.usercenter.dal.dao.UserInfoMapper;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import com.kelab.usercenter.dal.repo.UserSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserInfoRepoImpl implements UserInfoRepo {

    private final UserInfoMapper userInfoMapper;

    private final UserSubmitInfoRepo userSubmitInfoRepo;

    @Autowired(required = false)
    public UserInfoRepoImpl(UserInfoMapper userInfoMapper,
                            UserSubmitInfoRepo userSubmitInfoRepo) {
        this.userInfoMapper = userInfoMapper;
        this.userSubmitInfoRepo = userSubmitInfoRepo;
    }

    public List<UserInfoDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo) {
        List<UserInfoModel> userInfoModels = userInfoMapper.queryByIds(ids);
        if (userInfoModels == null || userInfoModels.size() == 0) {
            return Collections.emptyList();
        }
        List<UserInfoDomain> domains = new ArrayList<>(userInfoModels.size());
        userInfoModels.forEach(item -> domains.add(UserInfoConvert.modelToDomain(item)));
        if (withSubmitInfo) {
            List<Integer> userIds = userInfoModels.stream().map(UserInfoModel::getId).collect(Collectors.toList());
            List<UserSubmitInfoDomain> userSubmitInfoDomains = userSubmitInfoRepo.queryByUserIds(userIds);
            Map<Integer, UserSubmitInfoDomain> submitInfoDomainMap = userSubmitInfoDomains
                    .stream()
                    .collect(Collectors.toMap(UserSubmitInfoDomain::getUserId, (obj) -> obj));
            domains.forEach(item -> item.setSubmitInfo(submitInfoDomainMap.get(item.getId())));
        }
        return domains;
    }

    @Override
    public UserInfoDomain queryByUsername(String username, boolean withSubmitInfo) {
        UserInfoDomain domain = UserInfoConvert.modelToDomain(userInfoMapper.queryByUsername(username));
        if (domain != null && withSubmitInfo) {
            List<UserSubmitInfoDomain> userSubmitInfoDomains = userSubmitInfoRepo.queryByUserIds(Collections.singletonList(domain.getId()));
            domain.setSubmitInfo(userSubmitInfoDomains.get(0));
        }
        return domain;
    }

    @Override
    public UserInfoDomain queryByStudentId(String studentId, boolean withSubmitInfo) {
        UserInfoDomain domain = UserInfoConvert.modelToDomain(userInfoMapper.queryByStudentId(studentId));
        if (domain != null && withSubmitInfo) {
            List<UserSubmitInfoDomain> userSubmitInfoDomains = userSubmitInfoRepo.queryByUserIds(Collections.singletonList(domain.getId()));
            domain.setSubmitInfo(userSubmitInfoDomains.get(0));
        }
        return domain;
    }

    @Override
    public Integer queryTotal() {
        return userInfoMapper.queryTotal();
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
}
