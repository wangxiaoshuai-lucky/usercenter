package com.kelab.usercenter.config;

import com.kelab.util.ymlparse.annotation.Yaml;

public class AppSetting {

    @Yaml("cache.millisecond")
    public Long cacheMillisecond;

    @Yaml("jwt.secret_key")
    public String secretKey;

    @Yaml("jwt.millisecond")
    public Integer jwtMillisecond;

    @Yaml("jwt.jwt_issuer")
    public String jwtIssuer;

    @Yaml("jwt.jwt_aud")
    public String jwtAud;

    @Override
    public String toString() {
        return "AppSetting{" +
                "cacheMillisecond=" + cacheMillisecond +
                ", secretKey='" + secretKey + '\'' +
                ", millisecond=" + jwtMillisecond +
                ", jwtIssuer='" + jwtIssuer + '\'' +
                ", jwtAud='" + jwtAud + '\'' +
                '}';
    }
}
