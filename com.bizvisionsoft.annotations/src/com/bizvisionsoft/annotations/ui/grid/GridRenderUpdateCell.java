package com.bizvisionsoft.annotations.ui.grid;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
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
@Inherited
public @interface GridRenderUpdateCell {

	String PARAM_CELL = "格控件";

	String PARAM_COLUMN = "列定义";

	String PARAM_VALUE = "已取值";

	String PARAM_IMAGE = "已取图片";

}
