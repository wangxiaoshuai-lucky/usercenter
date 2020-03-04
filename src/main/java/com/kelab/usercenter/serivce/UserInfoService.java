package com.kelab.usercenter.serivce;

import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.model.UserInfoModel;

public interface UserInfoService {

    UserInfoDomain login(String username, String password);
}
