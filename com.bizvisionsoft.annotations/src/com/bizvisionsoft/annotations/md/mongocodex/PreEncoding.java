package com.bizvisionsoft.annotations.md.mongocodex;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

/**
 * 用于标识方法,在持久化之间被调用
 * 
 * @author zh@yaozheng.com.cn
 *
 */
@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreEncoding {


}