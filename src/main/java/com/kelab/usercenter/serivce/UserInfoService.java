package com.kelab.usercenter.serivce;

import com.kelab.usercenter.dal.model.UserInfoModel;

public interface UserInfoService {

    UserInfoModel login(String username, String password);
}
