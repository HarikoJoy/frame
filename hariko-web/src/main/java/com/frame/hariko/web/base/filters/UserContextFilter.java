package com.frame.hariko.web.base.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.frame.hariko.core.support.MyThreadContext;
import com.frame.hariko.core.support.UserContext;
import com.frame.hariko.web.base.constant.WebConstants;

public class UserContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (Objects.nonNull(request.getHeader(WebConstants.HEADER_FOR_USER_ID))) {
                String id = request.getHeader(WebConstants.HEADER_FOR_USER_ID);
                MyThreadContext.put(UserContext.ID, id);
            }
            String name = request.getHeader(WebConstants.HEADER_FOR_USER_NAME);
            String mobile = request.getHeader(WebConstants.HEADER_FOR_USER_MOBILE);
            String companyId = request.getHeader(WebConstants.HEADER_FOR_USER_COMPANY_ID);
            String companyName = request.getHeader(WebConstants.HEADER_FOR_USER_COMPANY_NAME);
            String roleIds = request.getHeader(WebConstants.HEADER_FOR_USER_ROLE_IDS);
            String roleNames = request.getHeader(WebConstants.HEADER_FOR_USER_ROLE_NAMES);
            String handleCenterId = request.getHeader(WebConstants.HEADER_FOR_USER_HANDLE_CENTER_ID);
            String handleCenterName = request.getHeader(WebConstants.HEADER_FOR_USER_HANDLE_CENTER_NAME);

            MyThreadContext.put(UserContext.NAME, name == null ? null : new String(name.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            MyThreadContext.put(UserContext.MOBILE, mobile);
            MyThreadContext.put(UserContext.COMPANY_ID, companyId);
            MyThreadContext.put(UserContext.COMPANY_NAME, companyName == null ? null : new String(companyName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            MyThreadContext.put(UserContext.ROLE_IDS, roleIds);
            MyThreadContext.put(UserContext.ROLE_NAMES, roleNames == null ? null : new String(roleNames.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            MyThreadContext.put(UserContext.HANDLE_CENTER_ID, handleCenterId);
            MyThreadContext.put(UserContext.HANDLE_CENTER_NAME, handleCenterName == null ? null : new String(handleCenterName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8));
            filterChain.doFilter(request, response);
        } finally {
            MyThreadContext.remove(UserContext.ID);
            MyThreadContext.remove(UserContext.NAME);
            MyThreadContext.remove(UserContext.MOBILE);
        }

    }

}
