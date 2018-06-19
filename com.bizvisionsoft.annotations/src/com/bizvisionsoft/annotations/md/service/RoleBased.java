package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记某个方法是返回用户角色的。 该方法可配合ServiceParam.CURRENT_USERID使用参数注解，注入当前的用户Id
 * 
 * 该方法可以返回null, 返回null表示对象不考虑用户角色。 返回List<String> 或 String[] 表示该用户对于该对象的角色
 * 返回空的List,表示该对象不限定使用角色
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(METHOD)
public @interface RoleBased {

}
