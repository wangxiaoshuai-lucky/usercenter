package com.kelab.usercenter;

import cn.wzy.verifyUtils.annotation.EnableVerify;
import com.kelab.usercenter.config.AppSetting;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableVerify
@MapperScan(basePackages = "com.kelab.usercenter.dal.dao")
@EnableFeignClients(basePackages = "com.kelab.usercenter.support.facade")
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
