package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 将模型的某个字段或方法暴露到表格用于为编辑action提供可用的编辑器。 该注解的方法或字段可返回以下的类型： <br/>
 * 1）String, 表示是否可编辑由action配置决定，返回的字符串是编辑器的name <br/>
 * 2）[String,boolean], String 为编辑器name，boolean表示是否可编辑（忽略action配置） <br/>
 * 3）[String,String],第一个String 是编辑用的编辑器 name，
 * 第二个是只读打开用的编辑器name，是否只读打开由action配置决定 <br/>
 * <br/>
 * 
 * 注解形式为：组件名/操作名
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface ReadEditorConfig {

	/**
	 * 
	 * @author hua
	 *
	 */
	String[] value();

}
