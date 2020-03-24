package com.kelab.usercenter.constant.enums;

public enum CompetitionTeamStatus {

    REJECT(1), PENDING(2), ACCEPT(3);

    private int value;

    CompetitionTeamStatus(int value) {
        this.value = value;
    }

    public static CompetitionTeamStatus valueOf(int value) {
        switch (value) {
            case 1:
                return REJECT;
            case 2:
                return PENDING;
            case 3:
                return ACCEPT;
        }
        throw new RuntimeException("CompetitionTeamStatus parse wrong");
    }

    public Integer value() {
        return this.value;
    }
}
