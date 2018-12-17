package com.frame.hariko.springboot.actuator.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.frame.hariko.springboot.actuator.DruidEndpoint;
import com.frame.hariko.springboot.actuator.mvc.DruidMvcEndpoint;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "endpoint.druid",name = "enable",havingValue = "true",matchIfMissing = true)
public class DruidEndpointAutoConfigure {

    @Bean
    public DruidEndpoint druidEndpoint(){
        return new DruidEndpoint();
    }

    @Bean
    @ConditionalOnWebApplication
    public DruidMvcEndpoint druidMvcEndpoint(){
        return new DruidMvcEndpoint(druidEndpoint());
    }
}
