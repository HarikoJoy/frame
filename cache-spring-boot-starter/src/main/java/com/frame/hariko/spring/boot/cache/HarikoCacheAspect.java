package com.frame.hariko.spring.boot.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;

@Aspect
public class HarikoCacheAspect {
	@Resource
	private RedisTemplate redisTemplate;

	@Pointcut("@annotation(com.frame.hariko.spring.boot.cache.annotation.HarikoCache)")
	public void harikoCachePointcut() {
	}

	@Around("harikoCachePointcut()")
	public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature signature = (MethodSignature) pjp.getSignature();
		Method method = signature.getMethod();
		
		Annotation[] annotationArray = method.getAnnotations();
		
		
		
		String key = null;
		int i = 0;

		for (Object value : pjp.getArgs()) {
			MethodParameter methodParam = new SynthesizingMethodParameter(method, i);
			Annotation[] paramAnns = methodParam.getParameterAnnotations();

			
			for (Annotation paramAnn : paramAnns) {
				
			}
			i++;
		}

		

		ValueOperations<String, Object> operations = redisTemplate.opsForValue();
		boolean hasKey = redisTemplate.hasKey(key);
		if (hasKey) {

			Object object = operations.get(key);

			return object;
		}

		Object object = pjp.proceed();
		operations.set(key, object, 30, TimeUnit.MINUTES); // 设置超时时间30分钟

		return object;
	}

}
