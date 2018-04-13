package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 获得自定义组件的容器
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Inherited
public @interface GetContainer {

}
