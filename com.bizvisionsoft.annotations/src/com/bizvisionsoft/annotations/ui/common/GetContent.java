package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ����Զ�����������ݣ������ݿ����������з���
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Inherited
public @interface GetContent {

	String value() default "";

}
