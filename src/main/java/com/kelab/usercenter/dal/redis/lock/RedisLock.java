package com.kelab.usercenter.dal.redis.lock;

import com.kelab.usercenter.constant.enums.DistributeKey;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class RedisLock {

    private static final String EXEC_RESULT = "1";

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();

    private final DefaultRedisScript<String> getLockRedisScript;


    private final DefaultRedisScript<String> releaseLockRedisScript;


    public RedisLock(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;

        getLockRedisScript = new DefaultRedisScript<>();
        getLockRedisScript.setResultType(String.class);
        releaseLockRedisScript = new DefaultRedisScript<>();
        releaseLockRedisScript.setResultType(String.class);
        //初始化装载 lua 脚本
        getLockRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/getLock.lua")));
        releaseLockRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/releaseLock.lua")));
    }

    /**
     * 尝试加锁
     */
    public boolean lock(DistributeKey key, String requestId, int expireTime, int retryTimes) {
        if (retryTimes <= 0)
            retryTimes = 1;
        try {
            int count = 0;
            while (true) {
                Object result = this.redisTemplate.execute(getLockRedisScript, stringRedisSerializer, stringRedisSerializer,
                        Collections.singletonList(key.toString()), requestId, String.valueOf(expireTime));
                if (EXEC_RESULT.equals(String.valueOf(result))) {
                    return true;
                } else {
                    count++;
                    if (retryTimes == count) {
                        return false;
                    } else {
                        Thread.sleep(100);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放锁
     */
    public boolean unLock(DistributeKey key, String requestId) {
        Object result = redisTemplate.execute(releaseLockRedisScript, stringRedisSerializer, stringRedisSerializer,
                Collections.singletonList(key.toString()), requestId);
        return EXEC_RESULT.equals(String.valueOf(result));
    }
}
