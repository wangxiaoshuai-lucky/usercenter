package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.convert.UserSubmitInfoConvert;
import com.kelab.usercenter.dal.dao.UserSubmitInfoMapper;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.model.UserSubmitInfoModel;
import com.kelab.usercenter.dal.repo.UserSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserSubmitInfoRepoImpl implements UserSubmitInfoRepo {

    private final UserSubmitInfoMapper userSubmitInfoMapper;

    @Autowired(required = false)
    public UserSubmitInfoRepoImpl(UserSubmitInfoMapper userSubmitInfoMapper) {
        this.userSubmitInfoMapper = userSubmitInfoMapper;
    }

    @Override
    public List<UserSubmitInfoDomain> queryByUserIds(List<Integer> userId) {
        List<UserSubmitInfoModel> userSubmitInfoModels = userSubmitInfoMapper.queryByUserIds(userId);
        if (CollectionUtils.isEmpty(userSubmitInfoModels)) {
            return Collections.emptyList();
        }
        return userSubmitInfoModels.stream().map(UserSubmitInfoConvert::modelToDomain).collect(Collectors.toList());
    }

    @Override
    public void save(UserSubmitInfoDomain record) {
        Integer id = userSubmitInfoMapper.save(UserSubmitInfoConvert.domainToModel(record));
        record.setId(id);
    }

    @Override
    public void update(Integer userId, boolean ac) {
        userSubmitInfoMapper.updateByUserId(userId, ac);
    }

    @Override
    public void delete(List<Integer> userIds) {
        userSubmitInfoMapper.delete(userIds);
    }
}
