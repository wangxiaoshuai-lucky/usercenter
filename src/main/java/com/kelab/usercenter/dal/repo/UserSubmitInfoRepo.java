package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;

public interface UserSubmitInfoRepo {

    /**
     * 通过 userId 查询用户提交情况
     *
     * @param userId
     * @return
     */
    UserSubmitInfoDomain queryByUserId(Integer userId);
}
