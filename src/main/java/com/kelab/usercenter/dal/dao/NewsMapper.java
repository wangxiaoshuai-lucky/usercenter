package com.kelab.usercenter.dal.dao;

import com.kelab.info.usercenter.query.NewsQuery;
import com.kelab.usercenter.dal.model.NewsModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsMapper {

    List<NewsModel> queryPage(@Param("query") NewsQuery query);

    Integer queryTotal(@Param("query") NewsQuery query);

    List<NewsModel> queryByIds(@Param("ids") List<Integer> ids);

    void update(@Param("record") NewsModel model);

    void save(@Param("record") NewsModel model);

    void delete(@Param("ids") List<Integer> ids);

    void addViewNumber(@Param("id") Integer id);
}
