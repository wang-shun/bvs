package com.bizvisionsoft.bruicommons.annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注为初始化方法，将在对象构造后被调用
 * 
 * @author hua
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface Init {

	/**
	 * 编辑对象时，该对象将被注入到init的方法的参数
	 */
	public final static String INPUT = "输入";

	String value() default "";

}
