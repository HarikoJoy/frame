package com.frame.hariko.springcloud.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.frame.hariko.springcloud.feign.FeignFilter;

@Configuration
@AutoConfigureAfter(FeignAutoConfiguration.class)
public class MyFeignAutoConfiguration {
    @Configuration
    @AutoConfigureAfter(FeignAutoConfiguration.class)
    protected static class FeignFilterAutoConfigure {
        private Environment properties;

        public FeignFilterAutoConfigure(Environment properties) {
            this.properties = properties;
        }

        @Bean
        public FeignFilter feignFilter() {
            // 先读取Apollo的系统变量app.id如果没有再去读取配置文件
            String applicationName = properties.getProperty("app.id");
            if (StringUtils.isEmpty(applicationName)) {
                applicationName = properties.getProperty("spring.application.name");
            }
            return new FeignFilter(applicationName);
        }
    }
}
