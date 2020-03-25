package com.kelab.usercenter.support.facade;


import com.kelab.info.usercenter.info.OnlineStatisticResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "service-problemcenter")
@RequestMapping("/problemcenter")
public interface ProblemCenterServiceSender {

    /**
     * 从题目中心拉取一天的submit ac
     */
    @GetMapping("/inner/countDay")
    Map<String, OnlineStatisticResult> countDay(@RequestParam Map<String, Object> param);
}
