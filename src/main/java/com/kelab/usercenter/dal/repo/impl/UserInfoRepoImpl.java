package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.convert.UserInfoConvert;
import com.kelab.usercenter.dal.dao.UserInfoMapper;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserInfoRepoImpl implements UserInfoRepo {

    private final UserInfoMapper userInfoMapper;

    @Autowired(required = false)
    public UserInfoRepoImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    public UserInfoDomain queryById(Integer id) {
        return UserInfoConvert.modelToDomain(userInfoMapper.queryById(id));
    }
}
