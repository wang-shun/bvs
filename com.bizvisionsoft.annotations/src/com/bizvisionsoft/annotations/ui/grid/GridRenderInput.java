package com.bizvisionsoft.annotations.ui.grid;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * �Զ���ı����Ⱦ�����ͨ���������������Input��ע��
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ FIELD })
public @interface GridRenderInput {

}
