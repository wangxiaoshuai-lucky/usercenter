package com.kelab.usercenter.dal.repo;

import com.kelab.usercenter.dal.domain.UserSubmitInfoDomain;

import java.util.List;

public interface UserSubmitInfoRepo {

    /**
     * 通过 userId 查询用户提交情况
     */
    List<UserSubmitInfoDomain> queryByUserIds(List<Integer> userIds);

    /**
     * 插入用户的提交记录
     */
    void save(UserSubmitInfoDomain record);

    /**
     * 插入用户的提交记录
     */
    void update(UserSubmitInfoDomain record);

    /**
     * 删除用户的提交数据
     */
    void delete(List<Integer> userIds);
}
