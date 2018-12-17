package com.frame.hariko.springboot.web.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.frame.hariko.core.exception.FastRuntimeException;
import com.frame.hariko.core.support.MyThreadContext;
import com.frame.hariko.web.base.views.ResponseVersion;

public class MerlinErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered {
    private static final Logger log = LoggerFactory.getLogger(MerlinErrorAttributes.class);

    private static final String ERROR_ATTRIBUTE = MerlinErrorAttributes.class.getName() + ".ERROR";

    private MessageSource messageSource;

    public MerlinErrorAttributes(){
    }

    public MerlinErrorAttributes(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        storeErrorAttributes(request, ex);
        if(ex instanceof FastRuntimeException){
            //返回一个modelAndview避免dispatchServlet将异常抛到tomcat容器(tomcat容器对于任何异常都打印Error),
            //此处对于业务异常则自己打印warn级别日志,控制日志级别主要是避免与应用其他Error异常区分;
            log.warn("resolveException handler FastRuntimeException:", ex);
            return new ModelAndView("/error");
        }else{
            //此处打印非业务异常的调用栈信息,并且带有traceId,因为异常到tomcat上没有打印出相关调用栈和traceId不好查问题;
            log.error("resolveException handler Exception:",ex);
            return null;
        }
    }

    private void storeErrorAttributes(HttpServletRequest request, Exception ex) {
        request.setAttribute(ERROR_ATTRIBUTE, ex);
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("version", ResponseVersion.VERSION_4.getVersion());
        errorAttributes.put("timestamp", System.currentTimeMillis());
        addCode(errorAttributes, requestAttributes);
        addErrorDetails(errorAttributes, requestAttributes, includeStackTrace);
        addPath(errorAttributes, requestAttributes);
        addTraceId(errorAttributes, requestAttributes);
        return errorAttributes;
    }

    private void addTraceId(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        String traceId = getAttribute(requestAttributes, MyThreadContext.TRACE_ID);
        errorAttributes.put("traceId", traceId);
    }

    protected void addCode(Map<String, Object> errorAttributes, WebRequest requestAttributes) {
        Throwable error = getError(requestAttributes);
        String code = getCode(error, requestAttributes, new boolean[1]);
        if (code == null) {
            errorAttributes.put("code", 999);
            errorAttributes.put("error", "None");
            return;
        }
        errorAttributes.put("code", code);
    }

    protected String getCode(Throwable error,RequestAttributes requestAttributes, boolean[] flag) {
        if (error == null) {
            if (flag[0]) {
                //业务异常使用code : 412
                requestAttributes.setAttribute("javax.servlet.error.status_code",HttpStatus.OK.value(),RequestAttributes.SCOPE_REQUEST);
                return HttpStatus.PRECONDITION_FAILED.toString();
            } else {
                Object code = getAttribute(requestAttributes, "javax.servlet.error.status_code");
                return Objects.nonNull(code)?code+"":null;
            }
        }
        //判断是否是参数校验异常
        if(error instanceof BindException){
            //http status
            requestAttributes.setAttribute("javax.servlet.error.status_code",HttpStatus.PRECONDITION_FAILED.value(),RequestAttributes.SCOPE_REQUEST);
            return HttpStatus.PRECONDITION_FAILED.toString();
        }
        //查看是否使用@ResponseStatus指定
        ResponseStatus responseStatus = AnnotatedElementUtils.findMergedAnnotation(error.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            //http status
            requestAttributes.setAttribute("javax.servlet.error.status_code", responseStatus.code().value(),RequestAttributes.SCOPE_REQUEST);
            return responseStatus.code().toString();
        }
        //反射查找错误Code
        Object code = null;
        try {
            Field field  = ReflectionUtils.findField(error.getClass(),"code");
            ReflectionUtils.makeAccessible(field);
            code = ReflectionUtils.getField(field,error);
        } catch (Exception e) {}

        if (code != null) {
            //http status
            requestAttributes.setAttribute("javax.servlet.error.status_code",HttpStatus.OK.value(),RequestAttributes.SCOPE_REQUEST);
            return code+"";
        }
        if (error instanceof FastRuntimeException) {
            flag[0] = true;
        }
        return getCode(error.getCause(), requestAttributes, flag);
    }

    protected void addErrorDetails(Map<String, Object> errorAttributes, WebRequest requestAttributes, boolean includeStackTrace) {
        Throwable error = getError(requestAttributes);
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = error.getCause();
            }
            addErrorMessage(errorAttributes, error);
            if (includeStackTrace) {
                errorAttributes.put("exception", error.getClass().getName());
                addStackTrace(errorAttributes, error);
            }
        }
        Object code = getAttribute(requestAttributes, "javax.servlet.error.status_code");
        try {
            errorAttributes.put("error", HttpStatus.valueOf(code+"").getReasonPhrase());
        } catch (Exception ex) {
            // Unable to obtain a reason
            errorAttributes.put("error", code+"");
        }
    }

    protected void addErrorMessage(Map<String, Object> errorAttributes, Throwable error) {
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            String msg = error.getMessage();
            if(error instanceof FastRuntimeException){
                //FastRuntimeException异常从messageSource文件中解析code
                FastRuntimeException exception = (FastRuntimeException) error;
                if((!StringUtils.isEmpty(exception.getCode())) && (messageSource != null)){
                    msg = messageSource.getMessage(exception.getCode(), exception.getArgs(), exception.getMessage(), null);
                }
            }
            errorAttributes.put("message",msg);
            return;
        }
        if (result.getErrorCount() > 0) {
            //与ResponseVo统一,错误详细信息放入data
            errorAttributes.put("data", result.getAllErrors());
            errorAttributes.put("message",getErrorMsg(result));
        } else {
            errorAttributes.put("message", "No errors");
        }
    }

    private String getErrorMsg(BindingResult result) {
        List<ObjectError> errors = result.getAllErrors();
        StringBuffer sb = new StringBuffer();
        int size = errors.size();
        for (int i = 0; i < size; i++) {
            ObjectError error = errors.get(i);
            sb.append(error.getDefaultMessage());
            if(i<size-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }

    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

    private void addStackTrace(Map<String, Object> errorAttributes, Throwable error) {
        StringWriter stackTrace = new StringWriter();
        error.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        errorAttributes.put("trace", stackTrace.toString());
    }

    private void addPath(Map<String, Object> errorAttributes,
                         RequestAttributes requestAttributes) {
        String path = getAttribute(requestAttributes, "javax.servlet.error.request_uri");
        if (path != null) {
            errorAttributes.put("path", path);
        }
    }

    @Override
    public Throwable getError(WebRequest requestAttributes) {
        Throwable exception = getAttribute(requestAttributes, ERROR_ATTRIBUTE);
        if (exception == null) {
            exception = getAttribute(requestAttributes, "javax.servlet.error.exception");
        }
        return exception;
    }

    @SuppressWarnings("unchecked")
    protected  <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }
}

