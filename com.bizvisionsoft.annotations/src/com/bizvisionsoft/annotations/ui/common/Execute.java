package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
@Inherited
public @interface Execute {

	String CONTEXT = "context";
	
	String CONTEXT_CONTENT = "content";

	String EVENT = "event";

	String ACTION = "action";
	
	String ROOT_CONTEXT_INPUT_OBJECT = "root_context_input_object";

	String PAGE_CONTEXT_INPUT_OBJECT = "page_context_input_object";

	String CONTEXT_INPUT_OBJECT = "context_input_object";
	
	String CONTEXT_SELECTION_1ST = "context_selection_1st";

	String CONTEXT_SELECTION = "context_selection";
	
	String CURRENT_USER = "current_user";

	String CURRENT_USER_ID = "current_user_id";

}
