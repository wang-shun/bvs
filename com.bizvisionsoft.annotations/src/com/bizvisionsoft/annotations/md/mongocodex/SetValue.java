package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * ���ڱ�ʶ�ֶλ򷽷� BSON->����
 * 
 * ��ʶ�ֶ�ʱ�����ֶε�ֵ�ɱ����ݿ��¼��Ӧname���ֶ�ֵ����
 * 
 * ��ʶ����ʱ���÷���������һ�����������ݿ��¼����Ϊ����ʱ����ø÷���������ò����������ݿ��Ӧname���ֶ�ֵ
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SetValue {

	String value() default "";

}