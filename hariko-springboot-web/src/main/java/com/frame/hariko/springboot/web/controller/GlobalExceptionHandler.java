package com.frame.hariko.springboot.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.frame.hariko.core.exception.FastRuntimeException;
import com.frame.hariko.core.support.MyThreadContext;

@RestControllerAdvice
@SuppressWarnings("rawtypes")
public class GlobalExceptionHandler {

	/**
	 * 所有通用异常处理
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public ErrorResponse defaultErrorHandler(HttpServletRequest request, Exception e) {
		return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e.toString());
	}

	/**
	 * 业务异常处理
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = FastRuntimeException.class)
	public ErrorResponse bizErrorHandler(HttpServletRequest request, FastRuntimeException e) {
		return new ErrorResponse(e.getCode(), e.getMessage());
	}

	/**
	 * 绑定参数错误处理
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = BindException.class)
	public ErrorResponse bindErrorHandler(HttpServletRequest request, BindException e) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
	}

	/**
	 * 处理请求对象属性不满足校验规则的异常信息
	 *
	 * @param request
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public ErrorResponse exception(HttpServletRequest request, MethodArgumentNotValidException e) {
		BindingResult result = e.getBindingResult();
		final List<FieldError> fieldErrors = result.getFieldErrors();
		StringBuilder builder = new StringBuilder();

		for (FieldError error : fieldErrors) {
			builder.append(error.getField()).append(":").append(error.getDefaultMessage()).append("\n");
		}
		return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), builder.toString());
	}

	/**
	 * 处理请求单个参数不满足校验规则的异常信息
	 *
	 * @param request
	 * @param e
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	public ErrorResponse constraintViolationExceptionHandler(HttpServletRequest request, ConstraintViolationException e) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
	}

	class ErrorResponse<T> {
		private String code;
		private String message;
		private T data;
		private String traceId;
		private long timestamp = System.currentTimeMillis();

		public ErrorResponse() {
		}

		public ErrorResponse(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public String getTraceId() {
			traceId = MyThreadContext.getTraceId();
			return traceId;
		}

		public void setTraceId(String traceId) {
			this.traceId = traceId;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}
}