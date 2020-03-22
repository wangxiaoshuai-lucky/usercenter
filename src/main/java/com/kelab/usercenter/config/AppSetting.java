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

    public static Integer jwtRefreshExpMillisecond;

    public static String jwtIssuer;

    public static String jwtAud;

    public static String mailHost;

    public static String mailUsername;

    public static String mailPassword;

    public static String mailSender;

    public static String accessFileExtend;

    @Value("${mail.smtp.host}")
    public void setMailHost(String mailHost) {
        AppSetting.mailHost = mailHost;
    }
    @Value("${mail.username}")
    public void setMailUsername(String mailUsername) {
        AppSetting.mailUsername = mailUsername;
    }
    @Value("${mail.password}")
    public void setMailPassword(String mailPassword) {
        AppSetting.mailPassword = mailPassword;
    }
    @Value("${mail.sender}")
    public void setMailSender(String mailSender) {
        AppSetting.mailSender = mailSender;
    }
    @Value("${file-upload.access-file-extend}")
    public void setAccessFileExtend(String accessFileExtend) {
        AppSetting.accessFileExtend = accessFileExtend;
    }


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

    @Value(("${jwt.refresh_exp_millisecond}"))
    public void setJwtRefreshExpMillisecond(Integer jwtRefreshExpMillisecond) {
        AppSetting.jwtRefreshExpMillisecond = jwtRefreshExpMillisecond;
    }

    @Value("${jwt.issuer}")
    public void setJwtIssuer(String jwtIssuer) {
        AppSetting.jwtIssuer = jwtIssuer;
    }

    @Value("${jwt.aud}")
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
