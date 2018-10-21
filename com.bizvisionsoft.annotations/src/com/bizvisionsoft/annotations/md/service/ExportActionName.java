package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记服务的某个方法是用于获取导出按钮名称
 * 
 * @author yj
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(METHOD)
public @interface ExportActionName {

	/**
	 * @author yj
	 *
	 */
	String[] value();

}
