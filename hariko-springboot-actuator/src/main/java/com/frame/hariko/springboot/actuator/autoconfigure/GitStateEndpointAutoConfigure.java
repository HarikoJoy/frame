package com.frame.hariko.springboot.actuator.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.frame.hariko.springboot.actuator.GitState;
import com.frame.hariko.springboot.actuator.GitStateEndpoint;
import com.frame.hariko.springboot.actuator.mvc.GitStateMvcEndpoint;

import java.util.Properties;

@Configuration
@ConditionalOnProperty(prefix = "endpoint.git", name = "enable", havingValue = "true", matchIfMissing = true)
public class GitStateEndpointAutoConfigure {

	@Autowired
	private InfoPropertiesConfiguration properties;

	@Bean
	@ConditionalOnMissingBean
	public GitStateEndpoint gitStateEndpoint() throws Exception {
		GitState gitInfo = this.properties.gitInfo();
		return new GitStateEndpoint(gitInfo);
	}

	@Bean
	@ConditionalOnWebApplication
	public GitStateMvcEndpoint gitStateMvcEndpoint() throws Exception {
		return new GitStateMvcEndpoint(gitStateEndpoint());
	}

	@Configuration
	protected static class InfoPropertiesConfiguration {

		@Autowired
		private final ConfigurableEnvironment environment = new StandardEnvironment();

		@Value("${spring.git.properties:classpath:git.properties}")
		private Resource gitProperties;

		public GitState gitInfo() throws Exception {
			Properties properties = new Properties();
			if (this.gitProperties.exists()) {
				properties = PropertiesLoaderUtils.loadProperties(this.gitProperties);
			}
			return new GitState(properties);
		}

		public ConfigurableEnvironment getEnvironment() {
			return environment;
		}
	}
}
