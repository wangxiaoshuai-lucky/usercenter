package com.kelab.usercenter.convert;

import com.kelab.info.usercenter.info.CompetitionInfo;
import com.kelab.info.usercenter.info.CompetitionStudentInfo;
import com.kelab.info.usercenter.info.CompetitionTeamInfo;
import com.kelab.info.usercenter.info.CompetitionTeamStudentInfo;
import com.kelab.usercenter.constant.enums.CompetitionTeamStatus;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.domain.CompetitionStudentDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamStudentDomain;
import com.kelab.usercenter.dal.model.CompetitionModel;
import com.kelab.usercenter.dal.model.CompetitionStudentModel;
import com.kelab.usercenter.dal.model.CompetitionTeamModel;
import org.springframework.beans.BeanUtils;

import java.util.stream.Collectors;

public class CompetitionConvert {
    public static CompetitionDomain modelToDomain(CompetitionModel model) {
        if (model == null) {
            return null;
        }
        CompetitionDomain domain = new CompetitionDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static CompetitionModel domainToModel(CompetitionDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionModel model = new CompetitionModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static CompetitionInfo domainToInfo(CompetitionDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionInfo info = new CompetitionInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static CompetitionDomain infoToDomain(CompetitionInfo info) {
        if (info == null) {
            return null;
        }
        CompetitionDomain domain = new CompetitionDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }

    public static CompetitionTeamDomain modelToDomain(CompetitionTeamModel model) {
        if (model == null) {
            return null;
        }
        CompetitionTeamDomain domain = new CompetitionTeamDomain();
        BeanUtils.copyProperties(model, domain);
        domain.setStatus(CompetitionTeamStatus.valueOf(model.getStatus()));
        return domain;
    }

    public static CompetitionTeamModel domainToModel(CompetitionTeamDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionTeamModel model = new CompetitionTeamModel();
        BeanUtils.copyProperties(domain, model);
        model.setStatus(domain.getStatus().value());
        return model;
    }

    public static CompetitionTeamInfo domainToInfo(CompetitionTeamDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionTeamInfo info = new CompetitionTeamInfo();
        BeanUtils.copyProperties(domain, info);
        info.setStatus(domain.getStatus().value());
        return info;
    }

    public static CompetitionTeamDomain infoToDomain(CompetitionTeamInfo info) {
        if (info == null) {
            return null;
        }
        CompetitionTeamDomain domain = new CompetitionTeamDomain();
        BeanUtils.copyProperties(info, domain);
        domain.setStatus(CompetitionTeamStatus.valueOf(info.getStatus()));
        return domain;
    }

    public static CompetitionStudentDomain modelToDomain(CompetitionStudentModel model) {
        if (model == null) {
            return null;
        }
        CompetitionStudentDomain domain = new CompetitionStudentDomain();
        BeanUtils.copyProperties(model, domain);
        return domain;
    }

    public static CompetitionStudentModel domainToModel(CompetitionStudentDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionStudentModel model = new CompetitionStudentModel();
        BeanUtils.copyProperties(domain, model);
        return model;
    }

    public static CompetitionStudentInfo domainToInfo(CompetitionStudentDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionStudentInfo info = new CompetitionStudentInfo();
        BeanUtils.copyProperties(domain, info);
        return info;
    }

    public static CompetitionStudentDomain infoToDomain(CompetitionStudentInfo info) {
        if (info == null) {
            return null;
        }
        CompetitionStudentDomain domain = new CompetitionStudentDomain();
        BeanUtils.copyProperties(info, domain);
        return domain;
    }

    public static CompetitionTeamStudentInfo domainToInfo(CompetitionTeamStudentDomain domain) {
        if (domain == null) {
            return null;
        }
        CompetitionTeamStudentInfo info = new CompetitionTeamStudentInfo();
        info.setCompetitionTeam(domainToInfo(domain.getTeamInfoDomain()));
        info.setMembers(domain.getMembers().stream().map(CompetitionConvert::domainToInfo).collect(Collectors.toList()));
        return info;
    }

    public static CompetitionTeamStudentDomain infoToDomain(CompetitionTeamStudentInfo info) {
        if (info == null) {
            return null;
        }
        CompetitionTeamStudentDomain domain = new CompetitionTeamStudentDomain();
        domain.setTeamInfoDomain(infoToDomain(info.getCompetitionTeam()));
        domain.setMembers(info.getMembers().stream().map(CompetitionConvert::infoToDomain).collect(Collectors.toList()));
        return domain;
    }
}