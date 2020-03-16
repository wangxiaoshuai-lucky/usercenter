package com.kelab.usercenter.dal.repo;

import com.kelab.info.usercenter.query.UserQuery;
import com.kelab.usercenter.dal.domain.UserInfoDomain;

import java.util.List;

public interface UserInfoRepo {

    /**
     * 通过 ids 查询用户(缓存)
     */
    List<UserInfoDomain> queryByIds(List<Integer> ids, boolean withSubmitInfo);

    /**
     * 查询用户（除 ids 例外）
     */
    List<UserInfoDomain> queryPage(UserQuery userQuery, boolean withSubmitInfo);

    /**
     * 查询 roleId 相关个数
     */
    Integer queryTotal(UserQuery userQuery);


    /**
     * 通过 username 查询用户
     */
    UserInfoDomain queryByUsername(String username, boolean withSubmitInfo);

    /**
     * 通过 studentId 查询用户
     */
    UserInfoDomain queryByStudentId(String studentId, boolean withSubmitInfo);


    /**
     * 用户注册
     */
    void save(UserInfoDomain userInfoDomain);

    /**
     * 更新
     */
    void update(UserInfoDomain userInfoDomain);

    /**
     * 删除用户
     */
    void delete(List<Integer> ids);
}
