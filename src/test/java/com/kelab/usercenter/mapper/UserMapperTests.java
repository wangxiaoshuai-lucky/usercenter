package com.kelab.usercenter.mapper;

import com.alibaba.fastjson.JSON;
import com.kelab.info.usercenter.info.UserInfo;
import com.kelab.usercenter.builder.UserInfoBuilder;
import com.kelab.usercenter.dal.domain.UserInfoDomain;
import com.kelab.usercenter.dal.repo.UserInfoRepo;
import io.jsonwebtoken.lang.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTests {

    @Autowired(required = false)
    private UserInfoRepo userInfoRepo;

    @Test
    @Rollback
    public void testSaveUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("test_username");
        userInfo.setEmail("111");
        userInfo.setStudentId("5120162154");
        userInfo.setPassword("ok12jsdj23");
        userInfo.setRealName("wzy");
        UserInfoDomain userInfoDomain = UserInfoBuilder.buildNewUser(userInfo);
        userInfoRepo.save(userInfoDomain);
        Assert.notNull(userInfoDomain.getId());
        Assert.notNull(userInfoDomain.getSubmitInfo().getId());
    }

    @Test
    @Rollback
    public void testQueryByIds() {
        List<UserInfoDomain> domains = userInfoRepo.queryByIds(Arrays.asList(1, 14290), true);
        for (UserInfoDomain userInfoDomain : domains) {
            System.out.println(JSON.toJSONString(userInfoDomain));
        }
    }

}
