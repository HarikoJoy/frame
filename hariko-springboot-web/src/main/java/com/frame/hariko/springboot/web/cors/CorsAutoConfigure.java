package com.frame.hariko.springboot.web.cors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.frame.hariko.web.base.constant.WebConstants;

@Configuration
@ConditionalOnProperty(prefix = CorsProperties.CORS_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({CorsProperties.class})
public class CorsAutoConfigure{

    @Autowired
    private CorsProperties corsProperties;

    @Bean
    @ConditionalOnMissingBean(CorsFilter.class)
    public OrderedCorsFilter orderedCorsFilter(){
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        if(corsProperties.getAllowedOrigins() == null) {
            corsProperties.addAllowedOrigin(CorsConfiguration.ALL);
        }
        if(corsProperties.getAllowedMethods() == null) {
            corsProperties.addAllowedMethod(CorsConfiguration.ALL);
        }

        if(corsProperties.getAllowedHeaders() == null) {
            corsProperties.addAllowedHeader(CorsConfiguration.ALL);
        }

        corsProperties.addExposedHeader(WebConstants.HEADER_FOR_USER_TOKEN);
        corsProperties.addExposedHeader(WebConstants.HEADER_FOR_TRACE_ID);
        configurationSource.registerCorsConfiguration("/**", corsProperties);
        return new OrderedCorsFilter(configurationSource);
    }
}
