package com.frame.hariko.spring.boot.cache.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@HarikoCache
public @interface RefreshCache {
	String name() default "";

	String namespace() default "";

	boolean setExpire() default false;

	long expireTime() default 60000l;

	String[] keyList() default {};

	Class<?>[] clazz();
}
