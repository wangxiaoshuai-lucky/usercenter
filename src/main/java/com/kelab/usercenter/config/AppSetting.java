package com.kelab.usercenter.config;

import com.kelab.util.ymlparse.annotation.Yaml;

public class AppSetting {

    @Yaml("jwt.secret_key")
    public String secretKey;

    @Yaml("jwt.millisecond")
    public Integer millisecond;

    @Yaml("jwt.jwt_issuer")
    public String jwtIssuer;

    @Yaml("jwt.jwt_aud")
    public String jwtAud;

    @Override
    public String toString() {
        return "AppSetting{" +
                "secretKey='" + secretKey + '\'' +
                ", millisecond=" + millisecond +
                ", jwtIssuer='" + jwtIssuer + '\'' +
                ", jwtAud='" + jwtAud + '\'' +
                '}';
    }
}
