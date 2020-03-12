package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.domain.UserInfoDomain;

import java.util.List;

public interface UserInfoRepo {

    /**
     * 通过 ids 查询用户
     */
    List<UserInfoDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo);


    /**
     * 通过 username 查询用户
     */
    UserInfoDomain queryByUsername(String username, boolean withSubmitInfo);

    /**
     * 通过 studentId 查询用户
     */
    UserInfoDomain queryByStudentId(String studentId, boolean withSubmitInfo);

    /**
     * 查询所有用户数量
     */
    Integer queryTotal();

    /**
     * 用户注册
     */
    void save(UserInfoDomain userInfoDomain);

    /**
     * 更新
     */
    void update(UserInfoDomain userInfoDomain);
}
