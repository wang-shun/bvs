package com.bizvisionsoft.annotations.ui.grid;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 自定义的表格渲染引擎可通过本参数获得表格配置Assembly的注入
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ FIELD })
@Inherited
public @interface GridRenderConfig {

}
