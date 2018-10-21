package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记服务的某个方法是用于导出数据的
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(METHOD)
public @interface Export {
	
	/**
	 * 默认的导出方法
	 */
	String DEFAULT = "default";

	/**
	 * @author hua
	 *
	 */
	String[] value();

}
