package com.bizvisionsoft.mongocodex.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识字段或方法
 * 
 * 标识字段时，该字段的值可被作为数据库记录对应name字段的值
 * 
 * 标识方法时，该方法必须有一个返回，对象反射为数据库记录时，该方饭返回的值作为数据库对应name的字段值
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface GetValue {

	String value() default "";

}