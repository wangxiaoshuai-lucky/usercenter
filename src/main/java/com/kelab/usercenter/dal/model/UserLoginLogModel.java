package com.kelab.usercenter.dal.model;

public class UserLoginLogModel {

    private Integer id;

    private Integer userId;

    private Long loginTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public UserLoginLogModel(Integer id, Integer userId, Long loginTime) {
        this.id = id;
        this.userId = userId;
        this.loginTime = loginTime;
    }

    public UserLoginLogModel() {
    }
}