package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.model.UserLoginLogModel;

public interface UserLoginLogRepo {

    /**
     * 保存登录日志
     *
     * @param record .
     */
    void save(UserLoginLogModel record);

}
