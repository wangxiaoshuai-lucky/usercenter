package com.kelab.usercenter.dal.model;


public class UserDayStatisticModel {

    private Integer id;

    private Integer acNum;

    private Integer submitNum;

    private Integer loginNum;

    private Long recordTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getLoginNum() {
        return loginNum;
    }

    public void setLoginNum(Integer loginNum) {
        this.loginNum = loginNum;
    }

    public Long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Long recordTime) {
        this.recordTime = recordTime;
    }
}
