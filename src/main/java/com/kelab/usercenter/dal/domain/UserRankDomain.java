package com.kelab.usercenter.dal.domain;

import com.kelab.info.usercenter.UserInfo;
import com.kelab.usercenter.constant.enums.TimeType;

public class UserRankDomain {

    private Integer id;

    private Integer userId;

    private Integer acNum;

    private Integer submitNum;

    private TimeType timeType;

    public UserRankDomain() {
    }

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

    public TimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(TimeType timeType) {
        this.timeType = timeType;
    }
}
