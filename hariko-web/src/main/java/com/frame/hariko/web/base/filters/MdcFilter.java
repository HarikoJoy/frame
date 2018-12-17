package com.frame.hariko.web.base.filters;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

import com.frame.hariko.core.support.MyThreadContext;
import com.frame.hariko.core.support.UserContext;

import ch.qos.logback.classic.helpers.MDCInsertingServletFilter;

public class MdcFilter extends MDCInsertingServletFilter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			MDC.put(MyThreadContext.MDC_LOG_LEVEL, (String) MyThreadContext.get(MyThreadContext.LOG_LEVEL));
			MDC.put(MyThreadContext.MDC_TRACE_ID, MyThreadContext.getTraceId());
			if (Objects.nonNull(MyThreadContext.get(UserContext.ID))) {
				MDC.put(MyThreadContext.MDC_USER_ID, (String) MyThreadContext.get(UserContext.ID));
			}
			try {
				super.doFilter(request, response, chain);
			} finally {
				MDC.remove(MyThreadContext.MDC_TRACE_ID);
				MDC.remove(MyThreadContext.MDC_USER_ID);
				MDC.remove(MyThreadContext.MDC_LOG_LEVEL);
			}
		} else {
			throw new ServletException("Only Http request supported.");
		}
	}

}
