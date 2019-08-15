package com.fulaan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解表示该参数是一个ObjectId类型的字符串
 * @author fourer
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectIdType {

	public boolean isRequire() default true;
	public String field() default "";
}
