package com.bizvisionsoft.bruiengine.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * �Զ���ı����Ⱦ����ı�ע�ⷽ�����ڱ����Ⱦ��ͷʱ������
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ METHOD })
public @interface GridRenderColumnHeader {

	public final static String PARAM_COLUMN_WIDGET = "�пؼ�";

	public final static String PARAM_COLUMN = "�ж���";

}
