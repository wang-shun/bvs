package com.bizvisionsoft.annotations.ui.grid;

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
public @interface GridRenderCompare {

	public final static String PARAM_ELEMENT1 = "e1";

	public final static String PARAM_ELEMENT2 = "e2";

}
