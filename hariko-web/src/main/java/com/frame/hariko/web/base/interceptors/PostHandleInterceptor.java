package com.frame.hariko.web.base.interceptors;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.frame.hariko.core.support.MyThreadContext;
import com.frame.hariko.web.base.views.ResponseVersion;

public class PostHandleInterceptor extends HandlerInterceptorAdapter {

    private  final  static int SUCCESS_RETURN_CODE = HttpServletResponse.SC_OK;
    private  final  static String SUCCESS_RETURN_MESSAGE = "success";



    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

        if(modelAndView != null) {
            Map<String, Object> model = modelAndView.getModel();
            model.put("status", SUCCESS_RETURN_CODE);
            model.put("message", SUCCESS_RETURN_MESSAGE);
            model.put("traceId", MyThreadContext.getTraceId());
            model.put("version", ResponseVersion.VERSION_4.getVersion());
        }
        
    }
}
