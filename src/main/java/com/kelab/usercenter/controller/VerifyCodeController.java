package com.kelab.usercenter.controller;


import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.BaseRetCodeConstant;
import com.kelab.info.usercenter.VerifyCodeInfo;
import com.kelab.usercenter.constant.enums.CacheConstant;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.util.verifycode.VerifyCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VerifyCodeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyCodeController.class);

    private final RedisCache redisCache;

    @Autowired
    public VerifyCodeController(RedisCache redisCache) {
        this.redisCache = redisCache;
    }

    /**
     * 获取验证码
     *
     * @return
     */
    @GetMapping("/pic.do")
    public JsonAndModel verifyPic(String logId, Integer operatorId) throws IOException {
        VerifyCodeUtils.ImgResult imgResult = VerifyCodeUtils.VerifyCode(80, 30, 4);
        redisCache.set(CacheConstant.VERIFY_CODE, imgResult.getUuid(), imgResult.getCode());
        VerifyCodeInfo verifyCode = new VerifyCodeInfo();
        verifyCode.setImg(imgResult.getImg());
        verifyCode.setUuid(imgResult.getUuid());
        return JsonAndModel.builder(BaseRetCodeConstant.SUCCESS)
                .data(verifyCode)
                .build();
    }
}
