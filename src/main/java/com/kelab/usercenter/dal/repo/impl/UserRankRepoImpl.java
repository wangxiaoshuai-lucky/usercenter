package com.kelab.usercenter.dal.repo.impl;

import com.kelab.info.base.query.PageQuery;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.convert.UserRankConvert;
import com.kelab.usercenter.dal.dao.UserRankMapper;
import com.kelab.usercenter.dal.domain.UserRankDomain;
import com.kelab.usercenter.dal.model.UserRankModel;
import com.kelab.usercenter.dal.repo.UserRankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRankRepoImpl implements UserRankRepo {


    private final UserRankMapper userRankMapper;

    @Autowired(required = false)
    public UserRankRepoImpl(UserRankMapper userRankMapper) {
        this.userRankMapper = userRankMapper;
    }

    @Override
    public List<UserRankDomain> queryPageByTimeType(TimeType timeType, PageQuery pageQuery) {
        List<UserRankModel> models = userRankMapper.queryPage(timeType.value(), pageQuery);
        List<UserRankDomain> domains = new ArrayList<>(models.size());
        models.forEach(item -> domains.add(UserRankConvert.modelToDomain(item)));
        return domains;
    }

    @Override
    public Integer total(TimeType timeType) {
        return userRankMapper.total(timeType.value());
    }
}
