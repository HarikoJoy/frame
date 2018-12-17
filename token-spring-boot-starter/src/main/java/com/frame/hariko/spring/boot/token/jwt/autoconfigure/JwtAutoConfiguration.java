package com.frame.hariko.spring.boot.token.jwt.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnMissingBean(JwtTokens.class)
public class JwtAutoConfiguration {
    @Bean
    public JwtTokens jwtTokens(JwtProperties properties) {
        return new JwtTokens(properties);
    }
}
