package com.bizvisionsoft.annotations.md.service;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * ��Ƿ����ĳ���������������ݼ���ѯ��
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ServiceParam {
	
	String SKIP = "skip";

	String LIMIT = "limit";
	
	String FILTER = "filter";

	String CONDITION = "condition";

	String _ID = "_id";

	String OBJECT = "object";

	String PARENT_ID = "parent_id";
	
	String PARENT_OBJECT = "parent";


	String value();

}
