package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
@Target(METHOD)
public @interface DataSet {
	
	String LIST = "list";

	String COUNT = "count";

	String INPUT = "input";

	String UPDATE = "update";

	String DELETE = "delete";
	
	String INSERT = "insert";

	String GET = "get";


	/**
	 * @author hua
	 *
	 */
	String[] value();

}
