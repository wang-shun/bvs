package com.bizvisionsoft.service.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 将模型的某个字段或方法暴露到表格记录的某个字段或者编辑器的某个字段 用于读取
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface ReadOptions {

	/**
	 * 空格不是必须的，只是为了好看，解释的时候会自动去掉前后多余的空格，如果该属性被多个表格或列使用时，可标记为数组
	 * 
	 * @author hua
	 *
	 */
	String[] value();

}
