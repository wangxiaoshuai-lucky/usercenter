package com.kelab.usercenter.controller;


import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.BaseRetCodeConstant;
import com.kelab.util.verifycode.VerifyCodeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VerifyCodeController {

    /**
     * 获取验证码
     *
     * @return
     */
    @RequestMapping(value = "/pic.do", method = {RequestMethod.GET})
    public JsonAndModel verifyPic() throws IOException {
        VerifyCodeUtils.ImgResult imgResult = VerifyCodeUtils.VerifyCode(80, 30, 4);
        // 存入redis缓存
        //iRedisTemplate.set(imgResult.getUuid(), imgResult.getCode(), 300);
        Map<String, Object> map = new HashMap<>();
        map.put("img", imgResult.getImg());
        map.put("uuid", imgResult.getUuid());
        return JsonAndModel.builder(BaseRetCodeConstant.SUCCESS)
                .data(map)
                .build();
    }
}
