package com.frame.hariko.job.elastic.autoconfigure;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties.JobPropertiesEnum;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Job解析类
 *
 * <p>
 * 从注解中解析任务信息初始化
 * <p>
 */
public class JobConfParser implements ApplicationContextAware {

	private Logger logger = LoggerFactory.getLogger(JobConfParser.class);

	private ZookeeperRegistryCenter zookeeperRegistryCenter;

	private List<ElasticJobProperties.JobProperties> jobPropertiesList;

	public JobConfParser(List<ElasticJobProperties.JobProperties> jobPropertiesList, ZookeeperRegistryCenter zookeeperRegistryCenter) {
		this.jobPropertiesList = jobPropertiesList;
		this.zookeeperRegistryCenter = zookeeperRegistryCenter;
	}

	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		if (CollectionUtils.isEmpty(jobPropertiesList)) {
			return;
		}
		for (ElasticJobProperties.JobProperties properties : jobPropertiesList) {
			Class<?> clz;
			try {
				clz = Class.forName(properties.getJobClass());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("class not found", e);
			}

			String jobTypeName = clz.getInterfaces()[0].getSimpleName();

			String jobClass = clz.getName();
			String jobName = properties.getJobClass();

			// 核心配置
			JobCoreConfiguration coreConfig = JobCoreConfiguration.newBuilder(jobName, properties.getCron(), properties.getShardingTotalCount())
					.shardingItemParameters(properties.getShardingItemParameters()).description(properties.getDescription()).failover(properties.getFailover())
					.jobParameter(properties.getJobParameter()).misfire(properties.getMisfire())
					.jobProperties(JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), properties.getJobExceptionHandler())
					.jobProperties(JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), properties.getExecutorServiceHandler()).build();

			// 不同类型的任务配置处理
			LiteJobConfiguration jobConfig;
			JobTypeConfiguration typeConfig = null;
			if (jobTypeName.equals("SimpleJob")) {
				typeConfig = new SimpleJobConfiguration(coreConfig, jobClass);
			}

			if (jobTypeName.equals("DataflowJob")) {
				typeConfig = new DataflowJobConfiguration(coreConfig, jobClass, properties.getStreamingProcess());
			}

			if (jobTypeName.equals("ScriptJob")) {
				typeConfig = new ScriptJobConfiguration(coreConfig, properties.getScriptCommandLine());
			}

			jobConfig = LiteJobConfiguration.newBuilder(typeConfig).overwrite(properties.getOverwrite()).disabled(properties.getDisabled())
					.monitorPort(properties.getMonitorPort()).monitorExecution(properties.getMonitorExecution())
					.maxTimeDiffSeconds(properties.getMaxTimeDiffSeconds()).jobShardingStrategyClass(properties.getJobShardingStrategyClass())
					.reconcileIntervalMinutes(properties.getReconcileIntervalMinutes()).build();

			List<BeanDefinition> elasticJobListeners = getTargetElasticJobListeners(properties);

			// 构建SpringJobScheduler对象来初始化任务
			BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
			factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			if ("ScriptJob".equals(jobTypeName)) {
				factory.addConstructorArgValue(null);
			} else {
				factory.addConstructorArgValue(ctx.getBean(clz));
			}
			factory.addConstructorArgValue(zookeeperRegistryCenter);
			factory.addConstructorArgValue(jobConfig);

			// 任务执行日志数据源，以名称获取
			if (StringUtils.hasText(properties.getEventTraceRdbDataSource())) {
				BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
				rdbFactory.addConstructorArgReference(properties.getEventTraceRdbDataSource());
				factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
			}
			factory.addConstructorArgValue(elasticJobListeners);
			DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();
			defaultListableBeanFactory.registerBeanDefinition("springJobScheduler", factory.getBeanDefinition());
			SpringJobScheduler springJobScheduler = (SpringJobScheduler) ctx.getBean("springJobScheduler");
			springJobScheduler.init();
			logger.info("【" + jobName + "】\t" + jobClass + "\tinit success");
		}
	}

	private List<BeanDefinition> getTargetElasticJobListeners(ElasticJobProperties.JobProperties properties) {
		List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
		if (StringUtils.hasText(properties.getListener())) {
			BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(properties.getListener());
			factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			result.add(factory.getBeanDefinition());
		}

		if (StringUtils.hasText(properties.getDistributedListener())) {
			BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(properties.getDistributedListener());
			factory.setScope(BeanDefinition.SCOPE_PROTOTYPE);
			factory.addConstructorArgValue(properties.getStartedTimeoutMilliseconds());
			factory.addConstructorArgValue(properties.getCompletedTimeoutMilliseconds());
			result.add(factory.getBeanDefinition());
		}
		return result;
	}
}
