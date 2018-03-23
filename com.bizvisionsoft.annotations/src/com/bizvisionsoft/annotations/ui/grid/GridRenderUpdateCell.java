package com.bizvisionsoft.annotations.ui.grid;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * �Զ���ı����Ⱦ����ı�ע�ⷽ�����ڱ����Ⱦ��Ԫ��ʱ������
 * 
 * @author hua
 *
 */
@Retention(RUNTIME)
@Target({ METHOD })
public @interface GridRenderUpdateCell {

	public final static String PARAM_CELL = "��ؼ�";

	public final static String PARAM_COLUMN = "�ж���";

	public final static String PARAM_VALUE = "��ȡֵ";

}
