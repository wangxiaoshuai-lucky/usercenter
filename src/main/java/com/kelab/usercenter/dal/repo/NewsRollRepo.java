package com.kelab.usercenter.dal.repo;

import com.kelab.info.usercenter.query.NewsQuery;
import com.kelab.info.usercenter.query.NewsRollQuery;
import com.kelab.usercenter.dal.domain.NewsDomain;
import com.kelab.usercenter.dal.domain.NewsRollDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsRollRepo {

    /**
     * 分页查询
     * 考虑到请求频繁,走缓存,同时在删除和更新的时候删除cache
     */
    List<NewsRollDomain> queryPage(NewsRollQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(NewsRollQuery query);

    /**
     * 走缓存
     */
    List<NewsRollDomain> queryByIds(List<Integer> ids);

    /**
     * 更新
     */
    void update(NewsRollDomain domain);

    /**
     * 添加
     */
    void save(NewsRollDomain domain);

    /**
     * 删除
     */
    void delete(List<Integer> ids);
}
