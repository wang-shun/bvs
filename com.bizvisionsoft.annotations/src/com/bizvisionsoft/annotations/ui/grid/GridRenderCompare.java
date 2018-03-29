package com.bizvisionsoft.annotations.ui.grid;

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
public @interface GridRenderCompare {

	public final static String PARAM_ELEMENT1 = "e1";

	public final static String PARAM_ELEMENT2 = "e2";

}
