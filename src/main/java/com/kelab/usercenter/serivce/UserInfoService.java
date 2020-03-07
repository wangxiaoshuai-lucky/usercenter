package com.kelab.usercenter.serivce;

import com.kelab.info.context.Context;
import com.kelab.info.usercenter.LoginResult;

public interface UserInfoService {

    LoginResult login(Context context, String username, String password, String verifyCode, String uuid);
}
