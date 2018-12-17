package com.frame.hariko.spring.boot.cache.redis.idgenerator;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisIdgeneratorAutoConfiguration {

    private Environment properties;

    public RedisIdgeneratorAutoConfiguration(Environment properties) {
        this.properties = properties;
    }

    @Bean
    public RedisIdGenerator redisIdGenerator(RedisTemplate<Object, Object> redisTemplate) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        Short businessId = properties.getProperty("idgenerator.businessid", Short.class);
        return new RedisIdGenerator(null, this.properties.getProperty("server.port", Integer.class, 8080), businessId, redisTemplate);
    }
}
