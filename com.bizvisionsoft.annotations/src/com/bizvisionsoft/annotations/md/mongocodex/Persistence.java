package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识字段是否保存到数据库，并且从数据库读出
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Persistence {

	public static String DEFAULT = "";
	
	String value() default DEFAULT;

}