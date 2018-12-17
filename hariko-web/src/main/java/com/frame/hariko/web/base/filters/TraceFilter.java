package com.frame.hariko.web.base.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.frame.hariko.core.support.MyThreadContext;

public class TraceFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest req = request;
		String parameter = req.getParameter("trace");
		if (parameter!=null & !"".equalsIgnoreCase(parameter)) {
			MyThreadContext.put("trace", "true");
		}
		try {
			filterChain.doFilter(request, response);
		} finally {
			MyThreadContext.remove("trace");
		}

	}

}
