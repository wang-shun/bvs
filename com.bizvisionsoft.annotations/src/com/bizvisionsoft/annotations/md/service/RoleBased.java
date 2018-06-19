package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ���ĳ�������Ƿ����û���ɫ�ġ� �÷��������ServiceParam.CURRENT_USERIDʹ�ò���ע�⣬ע�뵱ǰ���û�Id
 * 
 * �÷������Է���null, ����null��ʾ���󲻿����û���ɫ�� ����List<String> �� String[] ��ʾ���û����ڸö���Ľ�ɫ
 * ���ؿյ�List,��ʾ�ö����޶�ʹ�ý�ɫ
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(METHOD)
public @interface RoleBased {

}
