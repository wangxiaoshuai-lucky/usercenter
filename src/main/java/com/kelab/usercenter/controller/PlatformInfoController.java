package com.kelab.usercenter.controller;


import cn.wzy.verifyUtils.annotation.Verify;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.NewsInfo;
import com.kelab.info.usercenter.info.ScrollPictureInfo;
import com.kelab.info.usercenter.query.NewsQuery;
import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.convert.NewsConvert;
import com.kelab.usercenter.convert.ScrollPictureConvert;
import com.kelab.usercenter.serivce.PlatformInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlatformInfoController {

    private PlatformInfoService platformInfoService;

    public PlatformInfoController(PlatformInfoService platformInfoService) {
        this.platformInfoService = platformInfoService;
    }

    /**
     * 查询滚动图片
     */
    @GetMapping("/scrollPicture.do")
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public JsonAndModel queryScrollPicture(Context context, ScrollPictureQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(platformInfoService.queryScrollPicturePage(context, query))
                .build();
    }

    /**
     * 更新滚动图片
     */
    @PutMapping("/scrollPicture.do")
    @Verify(notNull = "record.id")
    public JsonAndModel updateScrollPicture(Context context, @RequestBody ScrollPictureInfo record) {
        platformInfoService.updateScrollPicture(context, ScrollPictureConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 添加滚动图片
     */
    @PostMapping("/scrollPicture.do")
    @Verify(notNull = {"record.picUrl", "record.newsTitle", "record.newsDesc", "record.newsUrl", "record.active"})
    public JsonAndModel saveScrollPicture(Context context, @RequestBody ScrollPictureInfo record) {
        platformInfoService.saveScrollPicture(context, ScrollPictureConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 删除滚动图片
     */
    @DeleteMapping("/scrollPicture.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel deleteScrollPicture(Context context, List<Integer> ids) {
        platformInfoService.deleteScrollPicture(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 查询新闻
     */
    @GetMapping("/news.do")
    @Verify(numberLimit = {"query.page [1, 100000]", "query.rows [1, 100000]"})
    public JsonAndModel queryNewsPage(Context context, NewsQuery query) {
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(platformInfoService.queryNewsPage(context, query))
                .build();
    }

    /**
     * 更新新闻
     */
    @PutMapping("/news.do")
    @Verify(notNull = "record.id")
    public JsonAndModel updateNews(Context context, @RequestBody NewsInfo record) {
        platformInfoService.updateNews(context, NewsConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 添加新闻
     */
    @PostMapping("/news.do")
    @Verify(notNull = {"record.title", "record.content", "record.picUrl", "context.operatorId"})
    public JsonAndModel saveScrollPicture(Context context, @RequestBody NewsInfo record) {
        platformInfoService.saveNews(context, NewsConvert.infoToDomain(record));
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 删除新闻
     */
    @DeleteMapping("/news.do")
    @Verify(sizeLimit = "ids [1, 200]")
    public JsonAndModel deleteNews(Context context, List<Integer> ids) {
        platformInfoService.deleteNews(context, ids);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

    /**
     * 访问量+1
     */
    @PutMapping("/news/viewnum.do")
    @Verify(notNull = "id")
    public JsonAndModel updateNewsViewNum(Context context, Integer id) {
        platformInfoService.addViewNumber(id);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS).build();
    }

}
