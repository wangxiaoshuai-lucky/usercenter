package com.kelab.usercenter.dal.dao;

import com.kelab.info.usercenter.query.NewsRollQuery;
import com.kelab.usercenter.dal.model.NewsRollModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NewsRollMapper {

    List<NewsRollModel> queryPage(@Param("query") NewsRollQuery query);

    Integer queryTotal(@Param("query") NewsRollQuery query);

    List<NewsRollModel> queryByIds(@Param("ids") List<Integer> ids);

    void update(@Param("record") NewsRollModel model);

    void save(@Param("record") NewsRollModel model);

    void delete(@Param("ids") List<Integer> ids);
}
