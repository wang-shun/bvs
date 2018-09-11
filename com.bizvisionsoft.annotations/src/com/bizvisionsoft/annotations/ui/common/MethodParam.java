package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 标记服务的某个方法是用于数据集查询的
 * 
 * @author hua
 *
 */
@Inherited
@Retention(RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MethodParam {

	String SKIP = "skip";

	String LIMIT = "limit";

	String FILTER = "filter";

	String FILTER_N_UPDATE = "filter_and_update";

	String CONDITION = "condition";

	String _ID = "_id";

	String OBJECT = "object";

	String PARENT_ID = "parent_id";

	String PARENT_OBJECT = "parent";

	String ROOT_CONTEXT_INPUT_OBJECT = "root_context_input_object";

	String PAGE_CONTEXT_INPUT_OBJECT = "page_context_input_object";

	String CONTEXT_INPUT_OBJECT = "context_input_object";

	String ROOT_CONTEXT_INPUT_OBJECT_ID = "root_context_input_object_id";

	String PAGE_CONTEXT_INPUT_OBJECT_ID = "page_context_input_object_id";

	String CONTEXT_INPUT_OBJECT_ID = "context_input_object_id";

	String CURRENT_USER = "current_user";

	String CURRENT_USER_ID = "current_user_id";

	String COMMAND = "command";

	String value();

}
