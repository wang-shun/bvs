package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 注解模型对象的可用的行为 注解方式 assemblyName # actionName，被注解的字段或方法返回boolean 表明该行为是否可用
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface Behavior {

	/**
	 * 
	 * @author hua
	 *
	 */
	String[] value() default "";

}
