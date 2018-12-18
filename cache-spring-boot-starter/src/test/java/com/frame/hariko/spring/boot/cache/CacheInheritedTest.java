package com.frame.hariko.spring.boot.cache;

import java.lang.reflect.Method;

import com.frame.hariko.spring.boot.cache.annotation.HarikoCache;
import com.frame.hariko.spring.boot.cache.annotation.QueryCache;

public class CacheInheritedTest {
	@QueryCache()
	public void testQueryCache() {
		
	}
	
	public static void main(String[] args) throws Exception{
		CacheInheritedTest test = new CacheInheritedTest();
		Method method = test.getClass().getMethod("testQueryCache");
		
		for(QueryCache cache : method.getAnnotationsByType(QueryCache.class)) {
			System.out.println(cache.getClass());
		}
	}
}
