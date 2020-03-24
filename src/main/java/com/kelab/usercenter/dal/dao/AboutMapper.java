package com.kelab.usercenter.dal.dao;

import com.kelab.info.base.query.BaseQuery;
import com.kelab.usercenter.dal.model.AboutModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AboutMapper {

    List<AboutModel> queryPage(@Param("query") BaseQuery query);

    Integer queryTotal();

    List<AboutModel> queryByIds(@Param("ids") List<Integer> ids);

    void update(@Param("record") AboutModel model);

    void save(@Param("record") AboutModel model);

    void delete(@Param("ids") List<Integer> ids);

    AboutModel queryPreRecord(@Param("order") Integer order);

    AboutModel queryNextRecord(@Param("order") Integer order);

    /**
     * set id1 ---- order1
     * set id2 ---- order2
     */
    void changeOrder(@Param("id1") Integer id1, @Param("order1") Integer order1,
                     @Param("id2") Integer id2, @Param("order2") Integer order2);
}
