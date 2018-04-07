package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ��ģ�͵�ĳ���ֶλ򷽷���¶������¼��ĳ���ֶλ��߱༭����ĳ���ֶ� ���ڶ�ȡ
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface Label {

	/**
	 * Ĭ������ע������ͨ�ñ�ǩ
	 */

	/**
	 * ����ע��������Ƶ�ͨ�ñ�ǩ
	 */
	public static final String NAME_LABEL = "namelabel";

	/**
	 * ����ע�����id��ͨ�ñ�ǩ
	 */
	public static final String ID_LABEL = "idlabel";
	
	
	/**
	 * 
	 * @author hua
	 *
	 */
	String value() default "";

}
