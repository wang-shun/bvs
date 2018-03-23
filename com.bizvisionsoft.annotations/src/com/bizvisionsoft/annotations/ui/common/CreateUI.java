package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注到方法上，该方法有且只有一个Composite 的参数。
 * 
 * @author hua
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
@Inherited
public @interface CreateUI {

}
