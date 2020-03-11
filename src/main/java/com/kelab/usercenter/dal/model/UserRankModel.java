package com.kelab.usercenter.dal.model;

public class UserRankModel {

    private Integer id;

    private Integer userId;

    private Integer acNum;

    private Integer submitNum;

    private Integer timeType;

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

    public Integer getAcNum() {
        return acNum;
    }

    public void setAcNum(Integer acNum) {
        this.acNum = acNum;
    }

    public Integer getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(Integer submitNum) {
        this.submitNum = submitNum;
    }

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }
}
