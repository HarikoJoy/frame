package com.frame.hariko.job.elastic.autoconfigure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "elastic-job.zk")
public class ZookeeperProperties {
	/**
	 * 连接Zookeeper服务器的列表,包括IP地址和端口号,多个地址用逗号分隔,如: host1:2181,host2:2181.
	 */
	private String serverLists;

	/**
	 * 命名空间.
	 */
	private String namespace = "merlin-job";

	/**
	 * 等待重试的间隔时间的初始值. 单位毫秒.
	 */
	private int baseSleepTimeMilliseconds = 1000;

	/**
	 * 等待重试的间隔时间的最大值. 单位毫秒.
	 */
	private int maxSleepTimeMilliseconds = 3000;

	/**
	 * 最大重试次数.
	 */
	private int maxRetries = 3;

	/**
	 * 会话超时时间. 单位毫秒.
	 */
	private int sessionTimeoutMilliseconds = 60000;

	/**
	 * 连接超时时间. 单位毫秒.
	 */
	private int connectionTimeoutMilliseconds = 15000;

	/**
	 * 连接Zookeeper的权限令牌. 缺省为不需要权限验证.
	 */
	private String digest;
}
