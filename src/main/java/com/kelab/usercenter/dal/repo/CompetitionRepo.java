package com.kelab.usercenter.dal.repo;

import com.kelab.info.base.query.BaseQuery;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CompetitionRepo {

    /**
     * 分页查询
     * 考虑到请求频繁,走缓存,同时在删除和更新的时候删除cache
     */
    List<CompetitionDomain> queryPage(BaseQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal();

    /**
     * 走缓存
     */
    List<CompetitionDomain> queryByIds(List<Integer> ids);

    /**
     * 更新
     */
    void update(CompetitionDomain domain);

    /**
     * 添加
     */
    void save(CompetitionDomain domain);

    /**
     * 删除
     */
    void delete(List<Integer> ids);
}
