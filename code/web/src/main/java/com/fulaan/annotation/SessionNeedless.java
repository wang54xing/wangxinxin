package com.fulaan.annotation;


import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 表示该方法不需要登录既可调用
 * @author fourer
 *
 */

@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionNeedless {

}
