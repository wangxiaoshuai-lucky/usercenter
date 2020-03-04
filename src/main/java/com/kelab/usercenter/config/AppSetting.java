package com.kelab.usercenter.config;

import com.kelab.util.ymlparse.annotation.Yaml;

public class AppSetting {

    @Yaml("server.port")
    public String port;

    @Yaml("spring.application.name")
    public String appName;

    @Override
    public String toString() {
        return "AppSetting{" +
                "port='" + port + '\'' +
                ", appName='" + appName + '\'' +
                '}';
    }
}
