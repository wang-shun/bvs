package com.bizvisionsoft.mongocodex.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * ���ڱ�ʶ�ֶλ򷽷�
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
public @interface GetValue {

	String value() default "";

}