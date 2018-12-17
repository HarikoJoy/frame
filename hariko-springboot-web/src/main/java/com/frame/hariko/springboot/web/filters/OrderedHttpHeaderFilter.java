package com.frame.hariko.springboot.web.filters;

import org.springframework.core.Ordered;

import com.frame.hariko.web.base.filters.HttpHeaderFilter;

public class OrderedHttpHeaderFilter extends HttpHeaderFilter implements Ordered{


    /**
     * 此filter必须在MdcFilter之前运行
     * @see OrderedMdcFilter
     */
    private int order = Ordered.HIGHEST_PRECEDENCE + 100;

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
