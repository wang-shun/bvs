package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 注解模型对象的方法用于检验选择的内容是否合法， 注解方式
 * assemblyName/fieldName，其中，assembly为编辑器组件名称，fieldName为编辑器字段名。被注解方法返回boolean
 * 表明该选择是否可用。
 * 
 * 被注解的方法接收注入一组标准的方法注入参数。
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(METHOD)
public @interface SelectionValidation {

	/**
	 * 
	 * @author hua
	 *
	 */
	String[] value() default "";

}
