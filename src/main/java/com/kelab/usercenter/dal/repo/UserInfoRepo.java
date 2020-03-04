package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.model.UserInfoModel;

public interface UserInfoRepo {

    /**
     * 通过 id 查询用户model
     * @param id
     * @return
     */
    UserInfoModel queryById(Integer id);
}
