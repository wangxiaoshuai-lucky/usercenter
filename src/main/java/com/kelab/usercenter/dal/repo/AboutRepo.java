package com.kelab.usercenter.dal.repo;

import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.usercenter.info.ChangeOrderInfo;
import com.kelab.usercenter.dal.domain.AboutDomain;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AboutRepo {

    /**
     * 分页查询
     * 考虑到请求频繁,走缓存,同时在删除和更新的时候删除cache
     */
    List<AboutDomain> queryPage(BaseQuery query);

    /**
     * 查询条数
     */
    Integer queryTotal();

    /**
     * 走缓存
     */
    List<AboutDomain> queryByIds(List<Integer> ids);

    /**
     * 更新
     */
    void update(AboutDomain domain);

    /**
     * 添加
     */
    void save(AboutDomain domain);

    /**
     * 删除
     */
    void delete(List<Integer> ids);

    /**
     * 调整顺序, 优先级： up > down
     */
    void changeAboutOrder(ChangeOrderInfo record);
}
