package com.kelab.usercenter.serivce;

import com.kelab.usercenter.repo.model.UserInfo;

public interface UserInfoService {

    UserInfo login(String username, String password);
}
