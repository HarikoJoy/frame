package com.frame.hariko.springboot.actuator;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "endpoints.git")
@Endpoint(id = "git")
public class GitStateEndpoint {


    private GitState gitState;

    public GitStateEndpoint(GitState gitState) {
        this.gitState = gitState;
    }

    /**
     * Create a new {@link GitStateEndpoint} instance.
     *
     */

    @ReadOperation
    public GitState invoke() {
        return this.gitState;
    }
}
