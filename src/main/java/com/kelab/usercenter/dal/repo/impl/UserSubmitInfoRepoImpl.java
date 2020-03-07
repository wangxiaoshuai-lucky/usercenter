package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.convert.UserSubmitInfoConvert;
import com.kelab.usercenter.dal.dao.UserSubmitInfoMapper;
import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;
import com.kelab.usercenter.dal.repo.UserSubmitInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserSubmitInfoRepoImpl implements UserSubmitInfoRepo {

    private final UserSubmitInfoMapper userSubmitInfoMapper;

    @Autowired(required = false)
    public UserSubmitInfoRepoImpl(UserSubmitInfoMapper userSubmitInfoMapper) {
        this.userSubmitInfoMapper = userSubmitInfoMapper;
    }

    @Override
    public UserSubmitInfoDomain queryByUserId(Integer userId) {
        return UserSubmitInfoConvert.modelToDomain(userSubmitInfoMapper.queryByUserId(userId));
    }
}
