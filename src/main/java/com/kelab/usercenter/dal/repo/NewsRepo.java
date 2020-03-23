package com.kelab.usercenter.dal.repo;

import com.kelab.info.usercenter.query.NewsQuery;
import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.dal.domain.NewsDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NewsRepo {

    /**
     * 分页查询
     * 考虑到请求频繁,走缓存,同时在删除和更新的时候删除cache
     */
    List<NewsDomain> queryPage(NewsQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal(NewsQuery query);

    /**
     * 走缓存
     */
    List<NewsDomain> queryByIds(List<Integer> ids);

    /**
     * 更新
     */
    void update(NewsDomain domain);

    /**
     * 添加
     */
    void save(NewsDomain domain);

    /**
     * 删除
     */
    void delete(List<Integer> ids);

    /**
     * 访问量+1
     * 此时运行小小的延迟
     * 增加访问量不刷新分页缓存
     */
    void addViewNumber(Integer id);
}
