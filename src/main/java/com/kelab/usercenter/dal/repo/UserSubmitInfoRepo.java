package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.model.UserInfoModel;

public interface UserSubmitInfoRepo {

    /**
     * 通过 userId 查询用户提交情况
     * @param id
     * @return
     */
    UserInfoModel q(Integer id);
}
