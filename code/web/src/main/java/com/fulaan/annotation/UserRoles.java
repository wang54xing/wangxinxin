package com.fulaan.annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pojo.user.UserRole;

/**
 * 用户角色控制；
 * 不允许在一个实例中既有value又有noValue两个参数
 * @author fourer
 *
 *
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRoles {
	/**
	 * 表示具有该角色的可以运行该方法
	 * @return
	 */
	UserRole[] value() default {};
	/**
	 * 表示不具有该角色的人可以运行该方法
	 * @return
	 */
	UserRole[] noValue() default {};
}
