package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��עΪ��ʼ�����������ڶ�����󱻵���
 * 
 * @author hua
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
@Inherited
public @interface Init {

	/**
	 * �༭����ʱ���ö��󽫱�ע�뵽init�ķ����Ĳ���
	 */
	public final static String INPUT = "����";

	String value() default "";

}
