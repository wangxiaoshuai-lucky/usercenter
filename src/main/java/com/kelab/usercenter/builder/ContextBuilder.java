package com.kelab.usercenter.builder;

import com.kelab.info.context.Context;

public class ContextBuilder {

    public static Context buildContext(String logId, Integer operatorId) {
        Context context = new Context();
        context.setLogId(logId);
        context.setOperatorId(operatorId);
        return context;
    }
}
