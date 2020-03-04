package com.kelab.usercenter;

import cn.wzy.verifyUtils.annotation.EnableVerify;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.util.ymlparse.DataSetter;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableVerify
@MapperScan(basePackages = "com.kelab.usercenter.repo.dao")
public class UserCenterApplication {

    public static AppSetting appSetting = new AppSetting();

    public static void main(String[] args) throws Exception {
        DataSetter.setData("application.yml", appSetting);
        System.out.println(appSetting);
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
