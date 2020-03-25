package com.kelab.usercenter.dal.repo.impl;

import com.kelab.info.base.query.PageQuery;
import com.kelab.usercenter.constant.enums.TimeType;
import com.kelab.usercenter.convert.UserRankConvert;
import com.kelab.usercenter.dal.dao.UserRankMapper;
import com.kelab.usercenter.dal.domain.UserRankDomain;
import com.kelab.usercenter.dal.model.UserRankModel;
import com.kelab.usercenter.dal.repo.UserRankRepo;
import com.kelab.usercenter.result.AcSubmitResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        if (CollectionUtils.isEmpty(models)) {
            return Collections.emptyList();
        }
        List<UserRankDomain> domains = models.stream().map(UserRankConvert::modelToDomain).collect(Collectors.toList());
        // 补充rank字段
        for (int i = 0; i < domains.size(); i++) {
            int preTotal = (pageQuery.getPage() - 1) * pageQuery.getRows();
            domains.get(i).setRank(i + preTotal + 1);
        }
        return domains;
    }

    @Override
    public Integer total(TimeType timeType) {
        return userRankMapper.total(timeType.value());
    }

    @Override
    public void delete(TimeType timeType) {
        userRankMapper.delete(timeType.value());
    }

    @Override
    public AcSubmitResult queryCount(TimeType timeType) {
        AcSubmitResult result = userRankMapper.queryCount(timeType.value());
        if (result == null) {
            result = new AcSubmitResult(0, 0);
        }
        return result;
    }
}
