package com.kelab.usercenter.constant.enums;

public enum TimeType {
    DAY(1), WEEK(2), MONTH(3);

    private int value;

    TimeType(int value) {
        this.value = value;
    }

    public static TimeType valueOf(int value) {
        switch (value) {
            case 1:
                return DAY;
            case 2:
                return WEEK;
            case 3:
                return MONTH;
        }
        throw new RuntimeException("TimeType parse wrong");
    }

    public Integer value() {
        return this.value;
    }
}
