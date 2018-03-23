package com.bizvisionsoft.annotations.ui.grid;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 自定义的表格渲染引擎的本注解方法将在表格渲染单元格时被调用
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ METHOD })
public @interface GridRenderUpdateCell {

	public final static String PARAM_CELL = "格控件";

	public final static String PARAM_COLUMN = "列定义";

	public final static String PARAM_VALUE = "已取值";

}
