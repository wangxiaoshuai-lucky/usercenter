package com.kelab.usercenter.serivce.impl;

import com.kelab.usercenter.repo.dao.UserInfoMapper;
import com.kelab.usercenter.repo.model.UserInfo;
import com.kelab.usercenter.serivce.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo login(String username, String password) {
        return userInfoMapper.queryById(1);
    }
}
