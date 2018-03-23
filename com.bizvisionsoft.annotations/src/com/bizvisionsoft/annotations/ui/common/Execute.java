package com.bizvisionsoft.annotations.ui.common;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface Execute {

	public final static String PARAM_CONTEXT = "context";

	public final static String PARAM_EVENT = "event";

}
