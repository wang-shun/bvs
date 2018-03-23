package com.bizvisionsoft.annotations.ui.common;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * �ڳ�ʼ����ǰע�뵽�ֶε�ֵ��ͨ���Ƿ���
 * 
 * @author hua
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

	String name() default "";

}