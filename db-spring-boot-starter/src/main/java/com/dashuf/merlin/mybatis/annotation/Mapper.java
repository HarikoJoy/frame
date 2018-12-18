/*******************************************************************************
 * rda
 ******************************************************************************/
package com.dashuf.merlin.mybatis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Documented
@Deprecated
public @interface Mapper {
		String value() default "";
}
