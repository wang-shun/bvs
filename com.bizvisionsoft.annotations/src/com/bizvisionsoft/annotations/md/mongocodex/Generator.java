package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识字段的自动生成器
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Generator {

	String DEFAULT_NAME = "CodexGenerator";

	String DEFAULT_KEY = "class";

	/**
	 * 通常是用于保存顺序号的集合名称
	 * @return
	 */
	String name();

	/**
	 * 通常是用于保存顺序号的序号名称
	 * @return
	 */
	String key();
	
	/**
	 * 生成器
	 * @return
	 */
	Class<? extends IAutoGenerator<?>> generator();
	
	/**
	 * 生成后的结果回调的方法名称，大小写敏感
	 * @return
	 */
	String callback();
	
	public static String NONE_CALLBACK = "";
	
}