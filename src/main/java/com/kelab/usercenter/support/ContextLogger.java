package com.kelab.usercenter.support;

import com.alibaba.fastjson.JSON;
import com.kelab.info.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextLogger {
    private final Logger LOGGER;

    private static final String CONTEXT_MSG = "context info:%s\n";

    public ContextLogger(Class<?> clazz) {
        LOGGER = LoggerFactory.getLogger(clazz);
    }

    public void info(Context context, String format, Object... objects) {
        String contextInfo = JSON.toJSONString(context);
        if (contextInfo.equals("{}")) {
            contextInfo = "暂无上下文信息";
        }
        String pre = String.format(CONTEXT_MSG, contextInfo);
        this.LOGGER.info(pre + format, objects);
    }

    public void error(Context context, String format, Object... objects) {
        String contextInfo = JSON.toJSONString(context);
        if (contextInfo.equals("{}")) {
            contextInfo = "暂无上下文信息";
        }
        String pre = String.format(CONTEXT_MSG, contextInfo);
        this.LOGGER.error(pre + format, objects);
    }

    public void debug(Context context, String format, Object... objects) {
        String contextInfo = JSON.toJSONString(context);
        if (contextInfo.equals("{}")) {
            contextInfo = "暂无上下文信息";
        }
        String pre = String.format(CONTEXT_MSG, contextInfo);
        this.LOGGER.debug(pre + format, objects);
    }
}
