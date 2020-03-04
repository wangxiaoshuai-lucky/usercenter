package com.kelab.usercenter.serivce.impl;

import com.kelab.usercenter.dal.dao.UserInfoMapper;
import com.kelab.usercenter.dal.model.UserInfoModel;
import com.kelab.usercenter.serivce.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserInfoService {

    private final UserInfoMapper userInfoMapper;

    @Autowired(required = false)
    public UserServiceImpl(UserInfoMapper userInfoMapper) {
        this.userInfoMapper = userInfoMapper;
    }

    @Override
    public UserInfoModel login(String username, String password) {
        return userInfoMapper.queryById(1);
    }
}
