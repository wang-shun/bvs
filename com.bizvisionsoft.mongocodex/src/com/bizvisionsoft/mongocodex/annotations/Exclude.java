package com.bizvisionsoft.mongocodex.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * ���ڱ�ʶ�ֶ��Ƿ�־û���Ĭ���ų�
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {

	boolean value() default true;

}