package com.frame.hariko.web.base.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import ooh.bravo.exception.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.frame.hariko.core.exception.FastRuntimeException;
import com.frame.hariko.web.base.utils.WebUtil;

@SuppressWarnings("unused")
public class MerlinWebExceptionResolver extends SimpleMappingExceptionResolver implements MessageSourceAware {

	private static final Logger log = LoggerFactory.getLogger(MerlinWebExceptionResolver.class);

	private MessageSource messageSource;

	public MerlinWebExceptionResolver() {
	}

	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
//    if(handler instanceof HandlerMethod && !ExceptionUtils.isHandledException(e, log)) {
//      logger.error("Catch a exception[{}] when invoke controller [{}]", new Object[]{e.getClass().getName(), handler, e});
//    }

		if (!WebUtil.isJsonRequest(request)) {
			return super.doResolveException(request, response, handler, e);
		} else {
			String exInfo = this.buildAjaxExceptionInfo(e);
			int status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
			if (e instanceof BindException) {
				status = HttpServletResponse.SC_BAD_REQUEST;
			}
			WebUtil.sendExceptionJsonRespone(response, status, exInfo);
			return new ModelAndView();
		}
	}

	private String buildAjaxExceptionInfo(Exception ex) {
		StringBuilder sb = new StringBuilder(64);
		if (ex instanceof FastRuntimeException) {
			String errorCode = ((FastRuntimeException) ex).getCode();
			String defaultMsg = ex.getMessage();
			String errorMsg = messageSource.getMessage(errorCode, null, defaultMsg, null);
			sb.append(errorMsg);
		} else {
			sb.append(org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage(ex));
		}
		return sb.toString();
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
