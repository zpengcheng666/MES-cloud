package com.miyu.module.mcc.utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisLock {

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean lock(String key, String value, long timeout, TimeUnit unit) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, timeout, unit);
        log.info("结果+++"+key+"++++" +success);
        return success != null && success;
    }

    public void unlock(String key, String value) {
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        if (value.equals(currentValue)) {
            log.info("释放成功"+key+"++++" +value);
            redisTemplate.delete(key);
        }
    }
}
