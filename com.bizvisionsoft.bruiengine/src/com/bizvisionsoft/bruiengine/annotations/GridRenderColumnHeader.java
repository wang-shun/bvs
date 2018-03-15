package com.bizvisionsoft.bruiengine.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 自定义的表格渲染引擎的本注解方法将在表格渲染列头时被调用
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ METHOD })
public @interface GridRenderColumnHeader {

	public final static String PARAM_COLUMN_WIDGET = "列控件";

	public final static String PARAM_COLUMN = "列定义";

}
