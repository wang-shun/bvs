package com.bizvisionsoft.service.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记服务的某个方法是用于数据集查询的
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ServiceParam {
	
	public static String SKIP = "skip";

	public static String LIMIT = "limit";
	
	public static String FILTER = "filter";

	public static String CONDITION = "condition";

	String value();

}
