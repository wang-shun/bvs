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
public @interface ReadValue {

	
	String TYPE = "_elementType";

	/**
	 * �ո��Ǳ���ģ�ֻ��Ϊ�˺ÿ������͵�ʱ����Զ�ȥ��ǰ�����Ŀո���������Ա����������ʹ��ʱ���ɱ��Ϊ����
	 * 
	 * @author hua
	 *
	 */
	String[] value() default "";

}
