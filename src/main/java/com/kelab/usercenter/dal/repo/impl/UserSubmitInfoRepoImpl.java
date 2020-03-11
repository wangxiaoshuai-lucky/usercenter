package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.convert.UserRankConvert;
import com.kelab.usercenter.convert.UserSubmitInfoConvert;
import com.kelab.usercenter.dal.dao.UserSubmitInfoMapper;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.model.UserSubmitInfoModel;
import com.kelab.usercenter.dal.repo.UserSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
        List<UserSubmitInfoDomain> domains = new ArrayList<>(userSubmitInfoModels.size());
        userSubmitInfoModels.forEach(item -> domains.add(UserSubmitInfoConvert.modelToDomain(item)));
        return domains;
    }

    @Override
    public void save(UserSubmitInfoDomain record) {
        Integer id = userSubmitInfoMapper.save(UserSubmitInfoConvert.domainToModel(record));
        record.setId(id);
    }
}
