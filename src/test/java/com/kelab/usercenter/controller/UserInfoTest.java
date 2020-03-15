package com.kelab.usercenter.controller;

import com.alibaba.fastjson.JSON;
import com.kelab.usercenter.constant.StatusMsgConstant;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.util.uuid.UuidUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class UserInfoTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RedisCache redisCache;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        //让每个测试用例启动之前都构建这样一个启动项
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void verifyPic() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/pic.do")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void loginSuccess() throws Exception {
        String uuid = UuidUtil.genUUID();
        redisCache.set(CacheBizName.VERIFY_CODE, uuid, "1111");
        mockMvc.perform(MockMvcRequestBuilders.get("/user/signin.do")
                .param("username", "wangzy")
                .param("password", "123456")
                .param("verifyCode", "1111")
                .param("uuid", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusMsgConstant.LOGIN_SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("wangzy"));
    }

    @Test
    public void pwdWrong() throws Exception {
        String uuid = UuidUtil.genUUID();
        redisCache.set(CacheBizName.VERIFY_CODE, uuid, "1111");
        mockMvc.perform(MockMvcRequestBuilders.get("/user/signin.do")
                .param("username", "wangzy")
                .param("password", "1234516")
                .param("verifyCode", "1111")
                .param("uuid", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusMsgConstant.PWD_ERROR));
    }

    @Test
    public void verifyWrong() throws Exception {
        String uuid = UuidUtil.genUUID();
        redisCache.set(CacheBizName.VERIFY_CODE, uuid, "1112");
        mockMvc.perform(MockMvcRequestBuilders.get("/user/signin.do")
                .param("username", "wangzy")
                .param("password", "123456")
                .param("verifyCode", "1111")
                .param("uuid", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusMsgConstant.VERIFY_CODE_ERROR));
    }

    @Test
    public void userNotExist() throws Exception {
        String uuid = UuidUtil.genUUID();
        redisCache.set(CacheBizName.VERIFY_CODE, uuid, "1111");
        mockMvc.perform(MockMvcRequestBuilders.get("/user/signin.do")
                .param("username", "wangzy11")
                .param("password", "123456")
                .param("verifyCode", "1111")
                .param("uuid", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusMsgConstant.USER_NOT_EXIST_ERROR));
    }

    @Test
    public void registerUserExisted() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "wangzy");
        params.put("studentId", "5120162154");
        params.put("password", "123456");
        params.put("realName", "wangzy");
        params.put("email", "1510460325@qq.com");
        mockMvc.perform(MockMvcRequestBuilders.post("/user.do")
                .content(JSON.toJSONString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusMsgConstant.USER_EXISTED_ERROR));
    }

    @Test
    public void registerStuIdExisted() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "wangzy1");
        params.put("studentId", "5120162154");
        params.put("password", "123456");
        params.put("realName", "wangzy");
        params.put("email", "1510460325@qq.com");
        params.put("logId", "111111111");
        mockMvc.perform(MockMvcRequestBuilders.post("/user.do")
                .content(JSON.toJSONString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusMsgConstant.STUDENT_ID_EXISTED_ERROR));
    }

    @Test
    public void registerSuccess() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("username", "testName");
        params.put("studentId", "testStudentId");
        params.put("password", "123456");
        params.put("realName", "testName");
        params.put("email", "1510460325@qq.com");
        params.put("logId", "111111111");
        mockMvc.perform(MockMvcRequestBuilders.post("/user.do")
                .content(JSON.toJSONString(params))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.status").value(StatusMsgConstant.LOGIN_SUCCESS));
    }

    @Test
    public void resetPassword() throws Exception {
        String uuid = UuidUtil.genUUID();
        redisCache.set(CacheBizName.VERIFY_CODE, uuid, "1111");
        mockMvc.perform(MockMvcRequestBuilders.get("/user/resetPasswd.do")
                .param("username", "wangzy")
                .param("verifyCode", "1111")
                .param("uuid", uuid)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(StatusMsgConstant.SUCCESS));
    }
}
