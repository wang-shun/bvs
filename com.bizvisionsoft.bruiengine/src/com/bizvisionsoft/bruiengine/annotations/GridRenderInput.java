package com.bizvisionsoft.bruiengine.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 自定义的表格渲染引擎可通过本参数获得数据Input的注入
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface GridRenderInput {

}
