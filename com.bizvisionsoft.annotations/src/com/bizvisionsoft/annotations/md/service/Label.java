package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 将模型的某个字段或方法暴露到表格记录的某个字段或者编辑器的某个字段 用于读取
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface Label {

	/**
	 * 默认用于注解对象的通用标签
	 */

	/**
	 * 用于注解对象名称的通用标签
	 */
	public static final String NAME_LABEL = "namelabel";

	/**
	 * 用于注解对象id的通用标签
	 */
	public static final String ID_LABEL = "idlabel";
	
	
	/**
	 * 
	 * @author hua
	 *
	 */
	String value() default "";

}
