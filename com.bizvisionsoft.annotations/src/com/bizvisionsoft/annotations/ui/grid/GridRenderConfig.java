package com.bizvisionsoft.annotations.ui.grid;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * �Զ���ı����Ⱦ�����ͨ����������ñ������Assembly��ע��
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ FIELD })
@Inherited
public @interface GridRenderConfig {

}
