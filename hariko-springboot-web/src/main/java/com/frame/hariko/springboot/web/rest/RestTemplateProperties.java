package com.frame.hariko.springboot.web.rest;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = RestTemplateProperties.PREFIX)
public class RestTemplateProperties {

    public static final String PREFIX = "spring.http.client";

    /**
     * A timeout value of 0 specifies an infinite timeout.
     * milliseconds
     */
    private int connectionTimeout = 0;

    /**
     * A timeout value of 0 specifies an infinite timeout.
     * milliseconds
     */
    private int readTimeout = 0;

    /**
     * Indicates whether this request factory should buffer the request body internally.
     * <p>Default is {@code true}. When sending large amounts of data via POST or PUT, it is
     * recommended to change this property to {@code false}, so as not to run out of memory.
     */
    private boolean bufferRequestBody = true;

    /**
     * A timeout value of 0 specifies an infinite timeout.
     * milliseconds
     */
    private int connectionRequestTimeout = 0;

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isBufferRequestBody() {
        return bufferRequestBody;
    }

    public void setBufferRequestBody(boolean bufferRequestBody) {
        this.bufferRequestBody = bufferRequestBody;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }
}
