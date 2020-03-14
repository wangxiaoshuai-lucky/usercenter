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
        String pre = String.format(CONTEXT_MSG, JSON.toJSONString(context));
        this.LOGGER.info("{}" + format, pre, objects);
    }

    public void error(Context context, String format, Object... objects) {
        String pre = String.format(CONTEXT_MSG, JSON.toJSONString(context));
        this.LOGGER.info("{}" + format, pre, objects);
    }

    public void debug(Context context, String format, Object... objects) {
        String pre = String.format(CONTEXT_MSG, JSON.toJSONString(context));
        this.LOGGER.debug("{}" + format, pre, objects);
    }
}
