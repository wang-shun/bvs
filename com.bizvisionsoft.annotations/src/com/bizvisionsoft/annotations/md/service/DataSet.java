package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ��Ƿ����ĳ���������������ݼ���ѯ��
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(METHOD)
public @interface DataSet {
	
	public static final String LIST = "list";

	public static final String COUNT = "count";

	public static final String INPUT = "input";

	public static final String UPDATE = "update";

	public static final String DELETE = "delete";
	
	public static final String INSERT = "insert";


	/**
	 * @author hua
	 *
	 */
	String[] value();

}
