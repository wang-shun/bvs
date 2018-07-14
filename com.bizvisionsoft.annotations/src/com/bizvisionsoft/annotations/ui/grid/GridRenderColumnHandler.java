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
public @interface GridRenderColumnHandler {


}
