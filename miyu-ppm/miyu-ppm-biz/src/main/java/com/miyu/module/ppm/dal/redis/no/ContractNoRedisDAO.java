package com.miyu.module.ppm.dal.redis.no;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 支付序号的 Redis DAO
 *
 * @author 芋道源码
 */
@Repository
public class ContractNoRedisDAO {
    /**
     * 合同
     */
    public static final String CONTRACT_NO_PREFIX = "HT";

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 生成序号
     *
     * @param prefix 前缀
     * @return 序号
     */
//    public String generate(String prefix) {
//        // 递增序号
//        String noPrefix = prefix + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
//        String key = RedisKeyConstants.CONTARCT_NO + noPrefix;
//        Long no = stringRedisTemplate.opsForValue().increment(key);
//        // 设置过期时间
//        stringRedisTemplate.expire(key, Duration.ofMinutes(1L));
//        return noPrefix + no;
//    }

}
