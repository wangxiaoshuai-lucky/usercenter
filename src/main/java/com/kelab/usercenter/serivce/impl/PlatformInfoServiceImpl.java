package com.kelab.usercenter.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.ScrollPictureInfo;
import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.convert.ScrollPictureConvert;
import com.kelab.usercenter.dal.domain.ScrollPictureDomain;
import com.kelab.usercenter.dal.repo.ScrollPictureRepo;
import com.kelab.usercenter.serivce.PlatformInfoService;
import com.kelab.usercenter.support.ContextLogger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlatformInfoServiceImpl implements PlatformInfoService {

    private ContextLogger contextLogger;

    private ScrollPictureRepo scrollPictureRepo;

    public PlatformInfoServiceImpl(ContextLogger contextLogger,
                                   ScrollPictureRepo scrollPictureRepo) {
        this.contextLogger = contextLogger;
        this.scrollPictureRepo = scrollPictureRepo;
    }

    @Override
    public PaginationResult<ScrollPictureInfo> queryPage(Context context, ScrollPictureQuery query) {
        List<ScrollPictureDomain> domains = scrollPictureRepo.queryPage(query);
        PaginationResult<ScrollPictureInfo> result = new PaginationResult<>();
        result.setPagingList(domainToInfo(domains));
        result.setTotal(scrollPictureRepo.queryTotal(query));
        return result;
    }

    @Override
    public void saveScrollPicture(Context context, ScrollPictureDomain record) {
        scrollPictureRepo.save(record);
    }

    @Override
    public void updateScrollPicture(Context context, ScrollPictureDomain record) {
        scrollPictureRepo.update(record);
    }

    @Override
    public void deleteScrollPicture(Context context, List<Integer> ids) {
        List<ScrollPictureDomain> old = scrollPictureRepo.queryByIds(ids);
        scrollPictureRepo.delete(ids);
        contextLogger.info(context, "删除轮播图：%s", JSON.toJSONString(old));
    }

    private List<ScrollPictureInfo> domainToInfo(List<ScrollPictureDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(ScrollPictureConvert::domainToInfo).collect(Collectors.toList());
    }
}
