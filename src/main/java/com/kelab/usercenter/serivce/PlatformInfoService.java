package com.kelab.usercenter.serivce;

import com.kelab.info.base.PaginationResult;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.NewsInfo;
import com.kelab.info.usercenter.info.ScrollPictureInfo;
import com.kelab.info.usercenter.query.NewsQuery;
import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.dal.domain.NewsDomain;
import com.kelab.usercenter.dal.domain.ScrollPictureDomain;

import java.util.List;

/**
 * 平台服务，比如关于，轮播图，新闻相关简单服务
 */
public interface PlatformInfoService {

    /**
     * 查询轮播图
     */
    PaginationResult<ScrollPictureInfo> queryScrollPicturePage(Context context, ScrollPictureQuery query);

    /**
     * 添加轮播图
     */
    void saveScrollPicture(Context context, ScrollPictureDomain record);

    /**
     * 更新轮播图
     */
    void updateScrollPicture(Context context, ScrollPictureDomain record);

    /**
     * 添加轮播图
     */
    void deleteScrollPicture(Context context, List<Integer> ids);

    /**
     * 查询新闻
     */
    PaginationResult<NewsInfo> queryNewsPage(Context context, NewsQuery query);

    /**
     * 添加新闻
     */
    void saveNews(Context context, NewsDomain record);

    /**
     * 更新新闻
     */
    void updateNews(Context context, NewsDomain record);

    /**
     * 添加新闻
     */
    void deleteNews(Context context, List<Integer> ids);

    /**
     * 访问量+1
     */
    void addViewNumber(Integer id);
}
