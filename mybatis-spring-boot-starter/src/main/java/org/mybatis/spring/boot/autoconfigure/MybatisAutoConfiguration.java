/*
 *    Copyright 2010-2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.mybatis.spring.boot.autoconfigure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.frame.hariko.tx.MerlinProxyTransactionManagementConfiguration;
import com.github.pagehelper.PageInterceptor;

@org.springframework.context.annotation.Configuration
@ConditionalOnClass({ SqlSessionFactory.class, SqlSessionFactoryBean.class })
@ConditionalOnBean(DataSource.class)
@EnableConfigurationProperties({ MybatisProperties.class, PageHelperProperties.class })
@EnableTransactionManagement(proxyTargetClass = true, order = 200)
@Import(MerlinProxyTransactionManagementConfiguration.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisAutoConfiguration {

	private static Logger log = LoggerFactory.getLogger(MybatisAutoConfiguration.class);

	private MybatisProperties mybatisProperties;

	private Interceptor[] interceptors;

	private ResourceLoader resourceLoader;

	private DatabaseIdProvider databaseIdProvider;

	private PageHelperProperties pageHelperProperties;

	private final List<ConfigurationCustomizer> configurationCustomizers;

	public MybatisAutoConfiguration(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader,
			ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider,
			PageHelperProperties pageHelperProperties) {
		this.mybatisProperties = properties;
		this.interceptors = interceptorsProvider.getIfAvailable();
		this.resourceLoader = resourceLoader;
		this.databaseIdProvider = databaseIdProvider.getIfAvailable();
		this.configurationCustomizers = configurationCustomizersProvider.getIfAvailable();
		this.pageHelperProperties = pageHelperProperties;
	}

	@PostConstruct
	public void checkConfigFileExists() {
		if (this.mybatisProperties.isCheckConfigLocation()) {
			Resource resource = this.resourceLoader.getResource(this.mybatisProperties.getConfigLocation());
			Assert.state(resource.exists(), "Cannot find config location: " + resource + " (please add config file or check your Mybatis " + "configuration)");
		}
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);
		factory.setVfs(SpringBootVFS.class);
		if (StringUtils.hasText(this.mybatisProperties.getConfigLocation())) {
			factory.setConfigLocation(this.resourceLoader.getResource(this.mybatisProperties.getConfigLocation()));
		}
		applyConfiguration(factory);
		if (this.mybatisProperties.getConfigurationProperties() != null) {
			factory.setConfigurationProperties(this.mybatisProperties.getConfigurationProperties());
		}
		if (!ObjectUtils.isEmpty(this.interceptors)) {
			factory.setPlugins(this.interceptors);
		}
		if (this.databaseIdProvider != null) {
			factory.setDatabaseIdProvider(this.databaseIdProvider);
		}
		if (StringUtils.hasLength(this.mybatisProperties.getTypeAliasesPackage())) {
			factory.setTypeAliasesPackage(this.mybatisProperties.getTypeAliasesPackage());
		}
		if (this.mybatisProperties.getTypeAliasesSuperType() != null) {
			factory.setTypeAliasesSuperType(this.mybatisProperties.getTypeAliasesSuperType());
		}
		if (StringUtils.hasLength(this.mybatisProperties.getTypeHandlersPackage())) {
			factory.setTypeHandlersPackage(this.mybatisProperties.getTypeHandlersPackage());
		}
		if (!ObjectUtils.isEmpty(this.mybatisProperties.resolveMapperLocations())) {
			factory.setMapperLocations(this.mybatisProperties.resolveMapperLocations());
		}

		return factory.getObject();
	}

	@Bean
	@ConfigurationProperties(prefix = PageHelperProperties.PAGEHELPER_PREFIX)
	public Properties pageHelperProperties() {
		return new Properties();
	}

	private void applyConfiguration(SqlSessionFactoryBean factory) {
		Configuration configuration = this.mybatisProperties.getConfiguration();
		if (configuration == null && !StringUtils.hasText(this.mybatisProperties.getConfigLocation())) {
			configuration = new Configuration();
		}
		if (configuration != null && !CollectionUtils.isEmpty(this.configurationCustomizers)) {
			for (ConfigurationCustomizer customizer : this.configurationCustomizers) {
				customizer.customize(configuration);
			}
		}
		// 分页插件
		PageInterceptor interceptor = new PageInterceptor();
		Properties properties = new Properties();
		// 先把一般方式配置的属性放进去
		properties.putAll(pageHelperProperties());
		// 在把特殊配置放进去，由于close-conn 利用上面方式时，属性名就是 close-conn 而不是 closeConn，所以需要额外的一步
		properties.putAll(this.pageHelperProperties.getProperties());
		interceptor.setProperties(properties);

		configuration.addInterceptor(interceptor);
		factory.setConfiguration(configuration);
	}

	@Bean
	@ConditionalOnMissingBean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory, this.mybatisProperties.getExecutorType());
	}

	/*
	 * @Bean(name={MERLIN_TXMANAGE, "transactionManager"})
	 * 
	 * @ConditionalOnMissingBean(name=MERLIN_TXMANAGE) public
	 * PlatformTransactionManager
	 * txManager(@Qualifier(DruidConstant.DATASOURCE_MERLIN)DataSource dataSource) {
	 * return new DataSourceTransactionManager(dataSource); }
	 */

	/**
	 * This will just scan the same base package as Spring Boot does. If you want
	 * more power, you can explicitly use
	 * {@link org.mybatis.spring.annotation.MapperScan} but this will get typed
	 * mappers working correctly, out-of-the-box, similar to using Spring Data JPA
	 * repositories.
	 */
	public static class AutoConfiguredMapperScannerRegistrar implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

		private BeanFactory beanFactory;

		private ResourceLoader resourceLoader;

		private ConfigurableEnvironment environment;

		/**
		 * 扫描{@link Mapper}标识的Mapper类,注入spring applicationContext
		 * 
		 * @param importingClassMetadata
		 * @param registry
		 */
		@Override
		public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

			ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

			try {
				Set<String> pkgsSet = new HashSet<>();
				List<String> pkgs = AutoConfigurationPackages.get(this.beanFactory);
				Map<String, Object> mybatisProperties = Binder.get(environment)
						.bind(MybatisProperties.MYBATIS_PREFIX, Bindable.mapOf(String.class, Object.class)).orElse(Collections.emptyMap());
				String basePackage = (String) mybatisProperties.get(".basePackage");
				if (StringUtils.hasText(basePackage)) {
					String[] ss = StringUtils.tokenizeToStringArray(basePackage, ",");
					pkgs = pkgs == null ? new ArrayList<>(ss.length) : pkgs;
					for (String s : ss) {
						pkgs.add(s);
					}
				}
				// remove duplicate package
				pkgsSet.addAll(pkgs);
				for (String pkg : pkgsSet) {
					log.debug("Found MyBatis auto-configuration package '" + pkg + "'");
				}

				if (this.resourceLoader != null) {
					scanner.setResourceLoader(this.resourceLoader);
				}

				scanner.setAnnotationClass(Mapper.class);
				scanner.registerFilters();
				scanner.doScan(pkgs.toArray(new String[pkgs.size()]));
			} catch (IllegalStateException ex) {
				log.debug("Could not determine auto-configuration " + "package, automatic mapper scanning disabled.");
				throw ex;
			}
		}

		@Override
		public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
			this.beanFactory = beanFactory;
		}

		@Override
		public void setResourceLoader(ResourceLoader resourceLoader) {
			this.resourceLoader = resourceLoader;
		}

		@Override
		public void setEnvironment(Environment environment) {
			this.environment = (ConfigurableEnvironment) environment;
		}

	}

	/**
	 * {@link org.mybatis.spring.annotation.MapperScan} ultimately ends up creating
	 * instances of {@link MapperFactoryBean}. If
	 * {@link org.mybatis.spring.annotation.MapperScan} is used then this
	 * auto-configuration is not needed. If it is _not_ used, however, then this
	 * will bring in a bean registrar and automatically register components based on
	 * the same component-scanning path as Spring Boot itself.
	 */
	@org.springframework.context.annotation.Configuration
	@Import({ AutoConfiguredMapperScannerRegistrar.class })
	@ConditionalOnMissingBean(MapperFactoryBean.class)
	public static class MapperScannerRegistrarNotFoundConfiguration {

		@PostConstruct
		public void afterPropertiesSet() {
			log.debug(String.format("No %s found.", MapperFactoryBean.class.getName()));
		}
	}
}
