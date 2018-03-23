package com.bizvisionsoft.annotations.ui.common;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 在初始化以前注入到字段的值，通常是服务
 * 
 * @author hua
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {

	String name() default "";

}