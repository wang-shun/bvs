package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ��ģ�͵�ĳ���ֶλ򷽷���¶���������Ϊ�༭action�ṩ���õı༭���� ��ע��ķ������ֶοɷ������µ����ͣ� <br/>
 * 1��String, ��ʾ�Ƿ�ɱ༭��action���þ��������ص��ַ����Ǳ༭����name <br/>
 * 2��[String,boolean], String Ϊ�༭��name��boolean��ʾ�Ƿ�ɱ༭������action���ã� <br/>
 * 3��[String,String],��һ��String �Ǳ༭�õı༭�� name��
 * �ڶ�����ֻ�����õı༭��name���Ƿ�ֻ������action���þ��� <br/>
 * <br/>
 * 
 * ע����ʽΪ�������/������
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface ReadEditorConfig {

	/**
	 * 
	 * @author hua
	 *
	 */
	String[] value();

}
