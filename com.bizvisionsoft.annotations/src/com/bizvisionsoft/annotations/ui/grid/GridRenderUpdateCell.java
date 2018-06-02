package com.bizvisionsoft.annotations.ui.grid;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
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
@Inherited
public @interface GridRenderUpdateCell {

	String PARAM_CELL = "��ؼ�";

	String PARAM_COLUMN = "�ж���";

	String PARAM_VALUE = "��ȡֵ";

	String PARAM_IMAGE = "��ȡͼƬ";

}
