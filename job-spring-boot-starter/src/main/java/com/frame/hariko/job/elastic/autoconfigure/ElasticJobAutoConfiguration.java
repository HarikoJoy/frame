package com.frame.hariko.job.elastic.autoconfigure;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "elastic-job.zk.server-lists")
@EnableConfigurationProperties({ ZookeeperProperties.class, ElasticJobProperties.class })
public class ElasticJobAutoConfiguration {

	@Autowired
	private ZookeeperProperties zookeeperProperties;

	@Autowired
	private ElasticJobProperties elasticJobProperties;

	/**
	 * 初始化Zookeeper注册中心
	 *
	 * @return
	 */
	@Bean(initMethod = "init")
	public ZookeeperRegistryCenter regCenter() {
		ZookeeperConfiguration zkConfig = new ZookeeperConfiguration(zookeeperProperties.getServerLists(), zookeeperProperties.getNamespace());
		zkConfig.setBaseSleepTimeMilliseconds(zookeeperProperties.getBaseSleepTimeMilliseconds());
		zkConfig.setConnectionTimeoutMilliseconds(zookeeperProperties.getConnectionTimeoutMilliseconds());
		zkConfig.setDigest(zookeeperProperties.getDigest());
		zkConfig.setMaxRetries(zookeeperProperties.getMaxRetries());
		zkConfig.setMaxSleepTimeMilliseconds(zookeeperProperties.getMaxSleepTimeMilliseconds());
		zkConfig.setSessionTimeoutMilliseconds(zookeeperProperties.getSessionTimeoutMilliseconds());
		return new ZookeeperRegistryCenter(zkConfig);
	}

	@Bean
	public JobConfParser jobConfParser(@Autowired ZookeeperRegistryCenter regCenter) {
		return new JobConfParser(elasticJobProperties.getJobs(), regCenter);
	}
}
