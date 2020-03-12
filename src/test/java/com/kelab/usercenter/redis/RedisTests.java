package com.kelab.usercenter.redis;

import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.constant.enums.CacheConstant;
import com.kelab.usercenter.dal.model.SiteSettingModel;
import com.kelab.usercenter.dal.redis.RedisCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisCache redisCache;


    @Test
    public void testCacheList() {
        List<SiteSettingModel> models = redisCache.cacheList(CacheConstant.SITE_SETTING, Collections.singletonList(1), SiteSettingModel.class, (keys -> {
            Map<Integer, SiteSettingModel> siteSettingModelMap = new HashMap<>();
            SiteSettingModel model = new SiteSettingModel();
            model.setId(1);
            model.setName("setting1");
            model.setValue("value1");
            siteSettingModelMap.put(1, model);
            return siteSettingModelMap;
        }));
        System.out.println(models);
    }

    @Test
    public void testCacheOne() {
        SiteSettingModel model = redisCache.cacheOne(CacheConstant.SITE_SETTING, Collections.singletonList(1), SiteSettingModel.class, (keys -> {
            SiteSettingModel model1 = new SiteSettingModel();
            model1.setId(1);
            model1.setName("setting1");
            model1.setValue("value2");
            return model1;
        }));
        System.out.println(model.getName() + model.getValue());
    }
}
