package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;

/**
 * ���ڱ�ʶ�ֶε��Զ�������
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Generator {

	String DEFAULT_NAME = "CodexGenerator";

	String DEFAULT_KEY = "class";

	/**
	 * ͨ�������ڱ���˳��ŵļ�������
	 * @return
	 */
	String name();

	/**
	 * ͨ�������ڱ���˳��ŵ��������
	 * @return
	 */
	String key();
	
	/**
	 * ������
	 * @return
	 */
	Class<? extends IAutoGenerator<?>> generator();
	
	/**
	 * ���ɺ�Ľ���ص��ķ������ƣ���Сд����
	 * @return
	 */
	String callback();
	
	public static String NONE_CALLBACK = "";
	
}