package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * ���ڱ�ʶ�ֶ��Ƿ񱣴浽���ݿ⣬���Ҵ����ݿ����
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Persistence {

	public static String DEFAULT = "";
	
	String value() default DEFAULT;

}