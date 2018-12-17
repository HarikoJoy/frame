package com.frame.hariko.springboot.actuator.mvc;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.frame.hariko.springboot.actuator.GitState;
import com.frame.hariko.springboot.actuator.GitStateEndpoint;

@ConfigurationProperties(prefix = "endpoints.git")
@EndpointWebExtension(endpoint = GitStateEndpoint.class)
public class GitStateMvcEndpoint{

    private GitStateEndpoint delegate;
    /**
     */
    public GitStateMvcEndpoint(GitStateEndpoint delegate) {
        this.delegate = delegate;
    }

    @ReadOperation
    public GitState handle() {
        return delegate.invoke();
    }
}
