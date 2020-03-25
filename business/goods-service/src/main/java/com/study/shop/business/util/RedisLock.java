package com.study.shop.business.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Tiger
 * @date 2020-03-10
 * @see com.study.shop.business.util
 **/
@Component
public class RedisLock {

    public final String LOCK_PREFIX = "redis_lock";

    public final int LOCK_EXPIRE = 3000;

    private final String SUCCESS = "1";

    private RedisTemplate redisTemplate;

    @Autowired
    private RedisLock(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取锁
     *
     * @param lockKey key
     * @param value   value
     * @return Boolean
     */
    public Boolean getLock(String lockKey, String value) {
        try {
            String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2])else return 0 end end";
            RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
            Object result = redisTemplate.execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(lockKey), value, LOCK_EXPIRE + "");
            assert result != null;
            if (SUCCESS.equals(result.toString())) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放锁
     *
     * @param lockKey key
     * @param value   value
     * @return Boolean
     */
    public Boolean releaseLock(String lockKey, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        Object result = redisTemplate.execute(redisScript, new StringRedisSerializer(), new StringRedisSerializer(), Collections.singletonList(lockKey), value);
        assert result != null;
        return SUCCESS.equals(result.toString());
    }
}
