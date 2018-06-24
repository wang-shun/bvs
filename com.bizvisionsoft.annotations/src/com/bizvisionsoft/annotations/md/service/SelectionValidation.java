package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ע��ģ�Ͷ���ķ������ڼ���ѡ��������Ƿ�Ϸ��� ע�ⷽʽ
 * assemblyName/fieldName�����У�assemblyΪ�༭��������ƣ�fieldNameΪ�༭���ֶ�������ע�ⷽ������boolean
 * ������ѡ���Ƿ���á�
 * 
 * ��ע��ķ�������ע��һ���׼�ķ���ע�������
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
