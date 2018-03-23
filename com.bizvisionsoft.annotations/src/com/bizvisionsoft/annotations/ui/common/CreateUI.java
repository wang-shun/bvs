package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ��ע�������ϣ��÷�������ֻ��һ��Composite �Ĳ�����
 * 
 * @author hua
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
@Inherited
public @interface CreateUI {

}
