package com.kelab.usercenter.repo.dao;

import com.kelab.usercenter.repo.model.UserInfo;
import org.apache.ibatis.annotations.Param;

public interface UserInfoMapper {

    UserInfo queryById(@Param("id") Integer id);

}
