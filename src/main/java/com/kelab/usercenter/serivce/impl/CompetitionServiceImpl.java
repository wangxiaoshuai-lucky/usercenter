package com.kelab.usercenter.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.kelab.info.base.PaginationResult;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.base.query.BaseQuery;
import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.CompetitionInfo;
import com.kelab.info.usercenter.info.CompetitionTeamStudentInfo;
import com.kelab.info.usercenter.query.CompetitionTeamQuery;
import com.kelab.usercenter.constant.enums.CompetitionTeamStatus;
import com.kelab.usercenter.convert.CompetitionConvert;
import com.kelab.usercenter.dal.domain.CompetitionDomain;
import com.kelab.usercenter.dal.domain.CompetitionStudentDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamDomain;
import com.kelab.usercenter.dal.domain.CompetitionTeamStudentDomain;
import com.kelab.usercenter.dal.repo.CompetitionRepo;
import com.kelab.usercenter.serivce.CompetitionService;
import com.kelab.usercenter.support.ContextLogger;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    private ContextLogger contextLogger;

    private CompetitionRepo competitionRepo;

    public CompetitionServiceImpl(ContextLogger contextLogger,
                                  CompetitionRepo competitionRepo) {
        this.contextLogger = contextLogger;
        this.competitionRepo = competitionRepo;
    }

    @Override
    public PaginationResult<CompetitionInfo> queryCompetitionPage(Context context, BaseQuery query) {
        PaginationResult<CompetitionInfo> result = new PaginationResult<>();
        List<Integer> totalIds = CommonService.totalIds(query);
        if (!CollectionUtils.isEmpty(totalIds)) {
            List<CompetitionDomain> domains = competitionRepo.queryByIds(totalIds);
            result.setPagingList(competitionDomainToInfo(domains));
            result.setTotal(domains.size());
        } else {
            List<CompetitionDomain> domains = competitionRepo.queryPage(query);
            result.setPagingList(competitionDomainToInfo(domains));
            result.setTotal(competitionRepo.queryTotal());
        }
        return result;
    }

    @Override
    public void saveCompetition(Context context, CompetitionDomain record) {
        competitionRepo.save(record);
    }

    @Override
    public void updateCompetition(Context context, CompetitionDomain record) {
        competitionRepo.updateTeam(record);
    }

    @Override
    public void deleteCompetition(Context context, List<Integer> ids) {
        List<CompetitionDomain> old = competitionRepo.queryByIds(ids);
        competitionRepo.delete(ids);
        contextLogger.info(context, "删除竞赛：%s", JSON.toJSONString(old));
    }

    @Override
    public PaginationResult<CompetitionTeamStudentInfo> queryTeamPage(Context context, CompetitionTeamQuery query) {
        PaginationResult<CompetitionTeamStudentInfo> result = new PaginationResult<>();
        result.setPagingList(teamStudentDomainToInfo(competitionRepo.queryTeamPage(query)));
        result.setTotal(competitionRepo.queryTeamTotal(query));
        return result;
    }

    @Override
    public String saveTeam(Context context, CompetitionTeamStudentDomain record) {
        CompetitionTeamDomain team = record.getTeamInfoDomain();
        List<CompetitionDomain> competitions = competitionRepo.queryByIds(Collections.singletonList(team.getCompetitionId()));
        if (CollectionUtils.isEmpty(competitions) || BooleanUtils.isNotTrue(competitions.get(0).getActive())) {
            return StatusMsgConstant.COMPETITION_NOT_EXIST_OR_CLOSE;
        }
        if (competitionRepo.queryTotalByName(team.getCompetitionId(), team.getTeamName()) > 0) {
            return StatusMsgConstant.TEAM_IS_EXISTED;
        }
        // 默认为等待
        team.setStatus(CompetitionTeamStatus.PENDING);
        competitionRepo.saveTeamInfo(record);
        return StatusMsgConstant.SUCCESS;
    }

    @Override
    public void updateTeam(Context context, CompetitionTeamDomain record) {
        competitionRepo.updateTeam(record);
    }

    @Override
    public ResponseEntity<byte[]> downloadTeamMessage(Context context, int competitionId) {
        List<CompetitionDomain> competitionDomains = competitionRepo.queryByIds(Collections.singletonList(competitionId));
        if (CollectionUtils.isEmpty(competitionDomains)) {
            throw new IllegalArgumentException("competition is not existed");
        }
        CompetitionTeamQuery query = new CompetitionTeamQuery();
        query.setCompetitionId(competitionId);
        query.setPage(1);
        query.setRows(competitionRepo.queryTeamTotal(query));
        List<CompetitionTeamStudentDomain> competitionTeamStudentDomains = competitionRepo.queryTeamPage(query);
        File xlsFile = new File("teamMessage.xls");
        this.writeMessage(xlsFile, competitionTeamStudentDomains);
        String fileName = new String((competitionDomains.get(0).getTitle() + ".xls").getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_UTF8_VALUE);
        try {
            return new ResponseEntity<>(FileUtils.readFileToByteArray(xlsFile), headers, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            xlsFile.delete();
        }
        return null;
    }

    /**
     * 将信息写入xls文件
     */
    public void writeMessage(File xlsFile, List<CompetitionTeamStudentDomain> competitionTeamList) {
        WritableWorkbook workbook = null;
        try {
            workbook = Workbook.createWorkbook(xlsFile);
            WritableSheet sheet = workbook.createSheet("sheet1", 0);
            int row = 0;
            int col = 0;
            sheet.addCell(new Label(col, row, "队伍名称"));
            for (int i = 1; i <= 3; i++) {
                sheet.addCell(new Label(++col, row, "队员" + i));
                sheet.addCell(new Label(++col, row, "学号"));
                sheet.addCell(new Label(++col, row, "班级"));
                sheet.addCell(new Label(++col, row, "尺码"));
                sheet.addCell(new Label(++col, row, "联系方式"));
                sheet.addCell(new Label(++col, row, "性别"));
                sheet.addCell(new Label(++col, row, "学院"));
            }
            if (competitionTeamList.size() != 0) {
                for (CompetitionTeamStudentDomain competitionTeam : competitionTeamList) {
                    if (competitionTeam.getTeamInfoDomain().getStatus() != CompetitionTeamStatus.ACCEPT) {
                        continue;
                    }
                    col = 0;
                    row++;
                    sheet.addCell(new Label(col, row, competitionTeam.getTeamInfoDomain().getTeamName()));
                    for (CompetitionStudentDomain student : competitionTeam.getMembers()) {
                        sheet.addCell(new Label(++col, row, student.getStuName()));
                        sheet.addCell(new Label(++col, row, student.getStuNum()));
                        sheet.addCell(new Label(++col, row, student.getStuClass()));
                        sheet.addCell(new Label(++col, row, student.getSize()));
                        sheet.addCell(new Label(++col, row, student.getPhone()));
                        sheet.addCell(new Label(++col, row, student.getSize().equals("0") ? "女" : "男"));
                        sheet.addCell(new Label(++col, row, student.getCollege()));
                    }
                }
            }
            workbook.write();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert workbook != null;
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private List<CompetitionInfo> competitionDomainToInfo(List<CompetitionDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(CompetitionConvert::domainToInfo).collect(Collectors.toList());
    }

    private List<CompetitionTeamStudentInfo> teamStudentDomainToInfo(List<CompetitionTeamStudentDomain> domains) {
        if (CollectionUtils.isEmpty(domains)) {
            return Collections.emptyList();
        }
        return domains.stream().map(CompetitionConvert::domainToInfo).collect(Collectors.toList());
    }
}
