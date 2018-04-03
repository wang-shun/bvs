package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ע��ģ�Ͷ���Ŀ��õ���Ϊ ע�ⷽʽ assemblyName # actionName����ע����ֶλ򷽷�����boolean ��������Ϊ�Ƿ����
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
