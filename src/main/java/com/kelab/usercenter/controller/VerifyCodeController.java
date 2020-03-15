package com.kelab.usercenter.controller;


import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.BaseRetCodeConstant;
import com.kelab.info.context.Context;
import com.kelab.usercenter.constant.enums.CacheBizName;
import com.kelab.usercenter.dal.redis.RedisCache;
import com.kelab.usercenter.result.VerifyCodeResult;
import com.kelab.util.verifycode.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class VerifyCodeController {


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
    public JsonAndModel verifyPic(Context context) throws IOException {
        VerifyCodeUtils.ImgResult imgResult = VerifyCodeUtils.VerifyCode(80, 30, 4);
        redisCache.set(CacheBizName.VERIFY_CODE, imgResult.getUuid(), imgResult.getCode());
        VerifyCodeResult verifyCode = new VerifyCodeResult();
        verifyCode.setImg(imgResult.getImg());
        verifyCode.setUuid(imgResult.getUuid());
        return JsonAndModel.builder(BaseRetCodeConstant.SUCCESS)
                .data(verifyCode)
                .build();
    }
}
