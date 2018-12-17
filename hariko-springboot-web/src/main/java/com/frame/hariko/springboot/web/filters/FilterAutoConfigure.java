package com.frame.hariko.springboot.web.filters;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpEncodingProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.frame.hariko.springboot.web.cors.CorsProperties;
import com.frame.hariko.web.base.filters.HttpHeaderFilter;
import com.frame.hariko.web.base.filters.MdcFilter;
import com.frame.hariko.web.base.filters.UserContextFilter;

@Configuration
@ConditionalOnClass({ HttpHeaderFilter.class, MdcFilter.class })
@EnableConfigurationProperties({ CorsProperties.class, HttpEncodingProperties.class })
public class FilterAutoConfigure {

	@Autowired
	private HttpEncodingProperties httpEncodingProperties;

	@Bean
	@ConditionalOnMissingBean(HttpHeaderFilter.class)
	public OrderedHttpHeaderFilter traceIdFilter() {
		return new OrderedHttpHeaderFilter();
	}

	@Bean
	@ConditionalOnMissingBean(MdcFilter.class)
	public OrderedMdcFilter mdcFilter() {
		return new OrderedMdcFilter();
	}

	@Bean
	@ConditionalOnMissingBean(UserContextFilter.class)
	public OrderedUserContextFilter userContextFilter() {
		return new OrderedUserContextFilter();
	}

	/**
	 * 配置默认的http encoding ， UTF-8 且强制request,response到utf-8;
	 * 若不需要默认实现则将enabled设置为false并自行注入一个CharacterEncodingFilter;
	 * 
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "spring.http.encoding", value = "enabled", matchIfMissing = true)
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
		filter.setEncoding(this.httpEncodingProperties.getCharset().name());
		filter.setForceRequestEncoding(true);
		filter.setForceResponseEncoding(true);
		return filter;
	}

	/**
	 * example for init by FilterResgitrationBean
	 * 
	 * @param servletContext
	 * @return
	 * @throws ServletException
	 */
	public FilterRegistrationBean filterRegistrationBean(ServletContext servletContext) throws ServletException {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.onStartup(servletContext);
		return registrationBean;
	}

	/**
	 * example for init by ServletRegistrationBean
	 * 
	 * @return
	 */
	public ServletRegistrationBean servletRegistrationBean() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
		return servletRegistrationBean;
	}
}
