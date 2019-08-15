package com.fulaan.annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户数据收集
 * @author fourer
 *
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserDataCollection {

	/**
	 * 页面标识
	 * @return
	 */
	public String pageTag() default "";
	
	/**
	 * 需要收集的参数
	 * @return
	 */
	public String[] params() default {};
	
	/**
	 * 参数是分别收集还是单独收集；默认一起收集
	 * @return
	 */
	public boolean isAllCollection() default true;
}
