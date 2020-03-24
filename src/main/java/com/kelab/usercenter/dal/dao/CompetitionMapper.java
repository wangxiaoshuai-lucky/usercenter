package com.kelab.usercenter.dal.dao;

import com.kelab.info.base.query.BaseQuery;
import com.kelab.usercenter.dal.model.CompetitionModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompetitionMapper {

    List<CompetitionModel> queryPage(@Param("query") BaseQuery query);

    Integer queryTotal();

    List<CompetitionModel> queryByIds(@Param("ids") List<Integer> ids);

    void update(@Param("record") CompetitionModel model);

    void save(@Param("record") CompetitionModel model);

    void delete(@Param("ids") List<Integer> ids);
}
