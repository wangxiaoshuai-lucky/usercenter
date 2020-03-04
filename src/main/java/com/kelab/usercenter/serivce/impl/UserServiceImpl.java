package com.kelab.usercenter.serivce.impl;

import com.kelab.usercenter.dal.dao.UserInfoMapper;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import com.kelab.usercenter.serivce.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserInfoService {

    private final UserInfoRepo userInfoRepo;

    @Autowired(required = false)
    public UserServiceImpl(UserInfoRepo userInfoRepo) {
        this.userInfoRepo = userInfoRepo;
    }

    @Override
    public UserInfoDomain login(String username, String password) {
        return userInfoRepo.queryById(1);
    }
}
