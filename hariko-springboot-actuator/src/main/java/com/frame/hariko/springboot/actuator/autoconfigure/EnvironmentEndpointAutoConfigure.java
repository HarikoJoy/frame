package com.frame.hariko.springboot.actuator.autoconfigure;

import org.springframework.boot.actuate.env.EnvironmentEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import com.frame.hariko.springboot.actuator.EnvironmentEndpointExt;

@Configuration
@ConditionalOnClass(EnvironmentEndpoint.class)
public class EnvironmentEndpointAutoConfigure implements EnvironmentAware {

    private ConfigurableEnvironment env;

    @Bean
    public EnvironmentEndpoint environmentEndpoint(){
        return new EnvironmentEndpointExt(env);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = (ConfigurableEnvironment) environment;
    }
}