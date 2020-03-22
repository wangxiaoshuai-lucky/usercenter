package com.kelab.usercenter.dal.dao;

import com.kelab.info.usercenter.query.ScrollPictureQuery;
import com.kelab.usercenter.dal.model.ScrollPictureModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScrollPictureMapper {

    List<ScrollPictureModel> queryPage(@Param("query") ScrollPictureQuery query);

    Integer queryTotal(@Param("query") ScrollPictureQuery query);

    List<ScrollPictureModel> queryByIds(@Param("ids") List<Integer> ids);

    void update(@Param("record") ScrollPictureModel model);

    void save(@Param("record") ScrollPictureModel model);

    void delete(@Param("ids") List<Integer> ids);
}
