package com.kelab.usercenter.dal.repo.impl;

import com.kelab.usercenter.dal.dao.UserLoginLogMapper;
import com.kelab.usercenter.dal.model.UserLoginLogModel;
import com.kelab.usercenter.dal.repo.UserLoginLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserLoginLogRepoImpl implements UserLoginLogRepo {

    private final UserLoginLogMapper userLoginLogMapper;

    @Autowired(required = false)
    public UserLoginLogRepoImpl(UserLoginLogMapper userLoginLogMapper) {
        this.userLoginLogMapper = userLoginLogMapper;
    }

    @Override
    public void save(UserLoginLogModel record) {
        userLoginLogMapper.save(record);
    }
}
