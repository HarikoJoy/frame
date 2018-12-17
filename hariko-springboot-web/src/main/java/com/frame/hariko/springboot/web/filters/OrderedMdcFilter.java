package com.frame.hariko.springboot.web.filters;

import org.springframework.core.Ordered;

import com.frame.hariko.web.base.filters.MdcFilter;

public class OrderedMdcFilter extends MdcFilter implements Ordered {

    private int order = Ordered.HIGHEST_PRECEDENCE + 200;

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
