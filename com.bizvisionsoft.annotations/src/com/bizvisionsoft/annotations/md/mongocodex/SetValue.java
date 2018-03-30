package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识字段或方法 BSON->对象
 * 
 * 标识字段时，该字段的值可被数据库记录对应name的字段值设置
 * 
 * 标识方法时，该方法必须有一个参数，数据库记录反射为对象时会调用该方饭，并向该参数传递数据库对应name的字段值
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SetValue {

	String value() default "";

}