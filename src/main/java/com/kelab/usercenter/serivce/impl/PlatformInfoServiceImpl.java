package com.kelab.usercenter.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.NewsInfo;
import com.kelab.info.usercenter.info.ScrollPictureInfo;
import com.kelab.info.usercenter.query.NewsQuery;
import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.convert.NewsConvert;
import com.kelab.usercenter.convert.ScrollPictureConvert;
import com.kelab.usercenter.dal.domain.NewsDomain;
import com.kelab.usercenter.dal.domain.ScrollPictureDomain;
import com.kelab.usercenter.dal.repo.NewsRepo;
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

    private NewsRepo newsRepo;

    public PlatformInfoServiceImpl(ContextLogger contextLogger,
                                   ScrollPictureRepo scrollPictureRepo,
                                   NewsRepo newsRepo) {
        this.contextLogger = contextLogger;
        this.scrollPictureRepo = scrollPictureRepo;
        this.newsRepo = newsRepo;
    }

    @Override
    public PaginationResult<ScrollPictureInfo> queryScrollPicturePage(Context context, ScrollPictureQuery query) {
        PaginationResult<ScrollPictureInfo> result = new PaginationResult<>();
        List<Integer> totalIds = CommonService.totalIds(query);
        if (!CollectionUtils.isEmpty(totalIds)) {
            List<ScrollPictureDomain> domains = scrollPictureRepo.queryByIds(totalIds);
            result.setPagingList(scrollPictureDomainToInfo(domains));
            result.setTotal(domains.size());
        } else {
            List<ScrollPictureDomain> domains = scrollPictureRepo.queryPage(query);
            result.setPagingList(scrollPictureDomainToInfo(domains));
            result.setTotal(scrollPictureRepo.queryTotal(query));
        }
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

    @Override
    public PaginationResult<NewsInfo> queryNewsPage(Context context, NewsQuery query) {
        PaginationResult<NewsInfo> result = new PaginationResult<>();
        List<Integer> totalIds = CommonService.totalIds(query);
        if (!CollectionUtils.isEmpty(totalIds)) {
            List<NewsDomain> domains = newsRepo.queryByIds(totalIds);
            result.setPagingList(newsDomainToInfo(domains));
            result.setTotal(domains.size());
        } else {
            List<NewsDomain> domains = newsRepo.queryPage(query);
            result.setPagingList(newsDomainToInfo(domains));
            result.setTotal(newsRepo.queryTotal(query));
        }
        return result;
    }

    @Override
    public void saveNews(Context context, NewsDomain record) {
        record.setPublishUser(context.getOperatorId());
        record.setPubTime(System.currentTimeMillis());
        record.setViewNum(0);
        newsRepo.save(record);
    }

    @Override
    public void updateNews(Context context, NewsDomain record) {
        newsRepo.update(record);
    }

    @Override
    public void deleteNews(Context context, List<Integer> ids) {
        List<NewsDomain> old = newsRepo.queryByIds(ids);
        newsRepo.delete(ids);
        contextLogger.info(context, "删除新闻：%s", JSON.toJSONString(old));
    }

    @Override
    public void addViewNumber(Integer id) {
        newsRepo.addViewNumber(id);
    }

    private List<ScrollPictureInfo> scrollPictureDomainToInfo(List<ScrollPictureDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(ScrollPictureConvert::domainToInfo).collect(Collectors.toList());
    }

    private List<NewsInfo> newsDomainToInfo(List<NewsDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(NewsConvert::domainToInfo).collect(Collectors.toList());
    }
}
