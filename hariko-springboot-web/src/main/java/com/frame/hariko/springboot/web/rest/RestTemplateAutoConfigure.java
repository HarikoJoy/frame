package com.frame.hariko.springboot.web.rest;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;


@Configuration
@ConditionalOnClass({ HttpClient.class })
@EnableConfigurationProperties({RestTemplateProperties.class})
@ConditionalOnProperty(prefix = RestTemplateProperties.PREFIX , name = "enable", havingValue = "true", matchIfMissing = false)
public class RestTemplateAutoConfigure {

    @Autowired
   private RestTemplateProperties restTemplateProperties;

    @Bean
    public  RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        //替换默认的iso-8859-1编码的string converter
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("utf-8"));
        List<HttpMessageConverter<?>> list = restTemplate.getMessageConverters();
        list.removeIf(httpMessageConverter -> httpMessageConverter instanceof StringHttpMessageConverter) ;
        list.add(converter);
        restTemplate.setMessageConverters(list);
        HttpComponentsClientHttpRequestFactory httpFactory = new HttpComponentsClientHttpRequestFactory();
        httpFactory.setConnectTimeout(restTemplateProperties.getConnectionTimeout());
        httpFactory.setBufferRequestBody(restTemplateProperties.isBufferRequestBody());
        httpFactory.setConnectionRequestTimeout(restTemplateProperties.getConnectionRequestTimeout());
        httpFactory.setReadTimeout(restTemplateProperties.getReadTimeout());
        restTemplate.setRequestFactory(httpFactory);
        return restTemplate;
    }
}
