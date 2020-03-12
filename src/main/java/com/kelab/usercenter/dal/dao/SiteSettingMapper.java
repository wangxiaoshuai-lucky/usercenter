package com.kelab.usercenter.dal.dao;

import com.kelab.usercenter.dal.model.SiteSettingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SiteSettingMapper {

    List<SiteSettingModel> queryByIds(@Param("ids") List<Integer> ids);
}
