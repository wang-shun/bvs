package com.bizvisionsoft.mongocodex.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识字段是否持久化，默认排除
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {

	boolean value() default true;

}