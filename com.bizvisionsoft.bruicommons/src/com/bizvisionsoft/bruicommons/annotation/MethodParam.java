package com.bizvisionsoft.bruicommons.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MethodParam {

	String value() default "";

}
