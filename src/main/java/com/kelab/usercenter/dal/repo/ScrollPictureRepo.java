package com.kelab.usercenter.dal.repo;

import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.dal.domain.ScrollPictureDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScrollPictureRepo {

    /**
     * 分页查询
     * 考虑到请求频繁,走缓存,同时在删除和更新的时候删除cache
     */
    List<ScrollPictureDomain> queryPage(ScrollPictureQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(ScrollPictureQuery query);

    /**
     * 走缓存
     */
    List<ScrollPictureDomain> queryByIds(List<Integer> ids);

    /**
     * 更新
     */
    void update(ScrollPictureDomain domain);

    /**
     * 添加
     */
    void save(ScrollPictureDomain domain);

    /**
     * 删除
     */
    void delete(List<Integer> ids);
}
