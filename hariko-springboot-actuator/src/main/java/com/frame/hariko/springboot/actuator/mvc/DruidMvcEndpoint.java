package com.frame.hariko.springboot.actuator.mvc;

import com.alibaba.druid.stat.DruidStatService;
import com.frame.hariko.springboot.actuator.DruidEndpoint;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;
import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ConfigurationProperties(prefix = "endpoints.druid")
@EndpointWebExtension(endpoint = DruidEndpoint.class)
public class DruidMvcEndpoint {
    private static final DruidStatService statService = DruidStatService.getInstance();

    private final DruidEndpoint delegate;
    private final long timeout;
    private Lock lock = new ReentrantLock();

    public DruidMvcEndpoint(DruidEndpoint delegate) {
        this.delegate = delegate;
        this.timeout = TimeUnit.SECONDS.toMillis(10);
    }

    public DruidMvcEndpoint(DruidEndpoint delegate, long timeout) {
        this.delegate = delegate;
        this.timeout = timeout;
    }
    
    public long getTimeout() {
		return timeout;
	}

	public Lock getLock() {
		return lock;
	}

	public void setLock(Lock lock) {
		this.lock = lock;
	}

	public static DruidStatService getStatservice() {
		return statService;
	}

	public DruidEndpoint getDelegate() {
		return delegate;
	}

	@ReadOperation
    public String handle(@Selector String name) {
        String temp = name;
        if (name.contains(".")) {
            temp = name.substring(0, name.indexOf("."));
        }
        name = "/" + temp + ".json";
        return statService.service(name);
    }

}
