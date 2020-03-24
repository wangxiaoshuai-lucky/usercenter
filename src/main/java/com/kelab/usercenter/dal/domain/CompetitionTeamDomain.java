package com.kelab.usercenter.dal.domain;

import com.kelab.usercenter.constant.enums.CompetitionTeamStatus;

public class CompetitionTeamDomain {
    private Integer id;

    private Integer competitionId;

    private String teamName;

    private String teacher;

    private CompetitionTeamStatus status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(Integer competitionId) {
        this.competitionId = competitionId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public CompetitionTeamStatus getStatus() {
        return status;
    }

    public void setStatus(CompetitionTeamStatus status) {
        this.status = status;
    }
}