package com.kelab.usercenter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
public class AppSetting {

    public static Long cacheMillisecond;

    public static String secretKey;

    public static Integer jwtMillisecond;

    public static String jwtIssuer;

    public static String jwtAud;

    @Value("${cache.millisecond}")
    public void setCacheMillisecond(Long cacheMillisecond) {
        AppSetting.cacheMillisecond = cacheMillisecond;
    }

    @Value("${jwt.secret_key}")
    public void setSecretKey(String secretKey) {
        AppSetting.secretKey = secretKey;
    }

    @Value("${jwt.millisecond}")
    public void setJwtMillisecond(Integer jwtMillisecond) {
        AppSetting.jwtMillisecond = jwtMillisecond;
    }

    @Value("${jwt.jwt_issuer}")
    public void setJwtIssuer(String jwtIssuer) {
        AppSetting.jwtIssuer = jwtIssuer;
    }

    @Value("${jwt.jwt_aud}")
    public void setJwtAud(String jwtAud) {
        AppSetting.jwtAud = jwtAud;
    }

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
