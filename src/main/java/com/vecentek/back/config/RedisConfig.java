package com.vecentek.back.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * @author ：EdgeYu
 * @version ：1.0
 * @since 2022-02-28 17:07
 */
@Configuration
public class RedisConfig {
    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    public void setRedis(RedisTemplate redisTemplate) {
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //设置序列化Key的实例化对象
        redisTemplate.setKeySerializer(stringRedisSerializer);
        //设置序列化Value的实例化对象
        redisTemplate.setValueSerializer(stringRedisSerializer);

        this.redisTemplate = redisTemplate;
    }
}
