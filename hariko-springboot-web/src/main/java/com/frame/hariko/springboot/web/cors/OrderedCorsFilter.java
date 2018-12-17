package com.frame.hariko.springboot.web.cors;

import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

public class OrderedCorsFilter extends CorsFilter implements Ordered{

    private int order = Ordered.HIGHEST_PRECEDENCE + 90;

    /**
     * Constructor accepting a {@link CorsConfigurationSource} used by the filter to find
     * the {@link CorsConfiguration} to use for each incoming request.
     *
     * @param configSource
     * @see UrlBasedCorsConfigurationSource
     */
    public OrderedCorsFilter(CorsConfigurationSource configSource) {
        super(configSource);
    }

    @Override
    public int getOrder() {
        return this.order;
    }


    /**
     * Set the order for this filter.
     * @param order the order to set
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
