package com.kelab.usercenter.dal.domain;

import java.util.ArrayList;
import java.util.List;

public class CompetitionTeamStudentDomain {

    /**
     * 团队
     */
    private CompetitionTeamDomain teamInfoDomain;
    /**
     * 成员
     */
    private List<CompetitionStudentDomain> members = new ArrayList<>();


    public CompetitionTeamDomain getTeamInfoDomain() {
        return teamInfoDomain;
    }

    public void setTeamInfoDomain(CompetitionTeamDomain teamInfoDomain) {
        this.teamInfoDomain = teamInfoDomain;
    }

    public List<CompetitionStudentDomain> getMembers() {
        return members;
    }

    public void setMembers(List<CompetitionStudentDomain> members) {
        this.members = members;
    }

    public CompetitionTeamStudentDomain() {
    }

    public CompetitionTeamStudentDomain(CompetitionTeamDomain teamInfoDomain, List<CompetitionStudentDomain> members) {
        this.teamInfoDomain = teamInfoDomain;
        this.members = members;
    }
}
