package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;

/**
 * ���ڱ�ʶ�ֶλ򷽷� ����-->BSON
 * 
 * ��ʶ�ֶ�ʱ�����ֶε�ֵ�ɱ���Ϊ���ݿ��¼��Ӧname�ֶε�ֵ
 * 
 * ��ʶ����ʱ���÷���������һ�����أ�������Ϊ���ݿ��¼ʱ���÷������ص�ֵ��Ϊ���ݿ��Ӧname���ֶ�ֵ
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface GetValue {

	String value() default "";

}