package com.kelab.usercenter.support.service;

import com.kelab.info.context.Context;
import com.kelab.info.usercenter.info.OnlineStatisticResult;
import com.kelab.usercenter.support.ParamBuilder;
import com.kelab.usercenter.support.facade.ProblemCenterServiceSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProblemCenterService {

    private ProblemCenterServiceSender problemCenterServiceSender;

    public ProblemCenterService(ProblemCenterServiceSender problemCenterServiceSender) {
        this.problemCenterServiceSender = problemCenterServiceSender;
    }

    public Map<String, OnlineStatisticResult> countDay(Context context, Long startTime, Long endTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        return problemCenterServiceSender.countDay(ParamBuilder.buildParam(context, param));
    }
}
