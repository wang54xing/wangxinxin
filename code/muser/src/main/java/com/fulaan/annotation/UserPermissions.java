package com.fulaan.annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户权限控制
 * @author fourer
 *
 */
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPermissions {

	int value();
}
