package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.domain.UserInfoDomain;

public interface UserInfoRepo {

    /**
     * 通过 id 查询用户model
     *
     * @param id 用户id
     * @param withSubmitInfo 是否附带提交信息
     * @return
     */
    UserInfoDomain queryById(Integer id, boolean withSubmitInfo);


    /**
     * 通过 username 查询用户model
     *
     * @param username username
     * @param withSubmitInfo 是否附带提交信息
     * @return
     */
    UserInfoDomain queryByUsername(String username, boolean withSubmitInfo);
}
