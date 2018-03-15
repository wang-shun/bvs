/*******************************************************************************
 * Copyright (c) 2012,2014 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Holger Staudacher - initial API and implementation
 ******************************************************************************/
package com.eclipsesource.jaxrs.consumer.internal;

import static javax.ws.rs.core.Response.Status.Family.CLIENT_ERROR;
import static javax.ws.rs.core.Response.Status.Family.SERVER_ERROR;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.message.internal.MediaTypes;

import com.eclipsesource.jaxrs.consumer.RequestException;

public class ResourceInvocationHandler implements InvocationHandler {

	private final Client client;
	private final String baseUrl;

	public ResourceInvocationHandler(String baseUrl, Configuration configuration) {
		this(baseUrl, ClientBuilder.newBuilder().withConfig(configuration).sslContext(ClientHelper.createSSLContext())
				.hostnameVerifier(ClientHelper.createHostNameVerifier()).build());
	}

	public ResourceInvocationHandler(String baseUrl, Client client) {
		this.client = client;
		this.baseUrl = baseUrl;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] parameter) throws Throwable {
		RequestConfigurer configurer = new RequestConfigurer(client, baseUrl, method, parameter);
		return sendRequest(method, parameter, configurer);
	}

	private Object sendRequest(Method method, Object[] parameter, RequestConfigurer configurer) {
		Object result = null;
		Builder request = configurer.configure();
		if (method.isAnnotationPresent(GET.class)) {
			result = sendGetRequest(configurer, method, request);
		}
		if (method.isAnnotationPresent(POST.class)) {
			result = sendPostRequest(configurer, method, parameter, request);
		}
		if (method.isAnnotationPresent(PUT.class)) {
			result = sendPutRequest(configurer, method, parameter, request);
		}
		if (method.isAnnotationPresent(DELETE.class)) {
			result = sendDeleteRequest(configurer, method, request);
		}
		if (method.isAnnotationPresent(HEAD.class)) {
			result = sendHeadRequest(configurer, method, request);
		}
		if (method.isAnnotationPresent(OPTIONS.class)) {
			result = sendOptionsRequest(configurer, method, request);
		}
		return result;
	}

	private Object getResponseReturn(Response response, Method method) {
		Type type = method.getGenericReturnType();
		Class<?> clas = method.getReturnType();
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return response.readEntity(new GenericType(parameterizedType));
		} else {
			return response.readEntity(clas);
		}
	}

	private Object sendGetRequest(RequestConfigurer configurer, Method method, Builder request) {
		checkHasNoFormParameter(method);
		Response response = request.get();
		validateResponse(configurer, response, "GET");
		return getResponseReturn(response, method);
	}

	private Object sendPostRequest(RequestConfigurer configurer, Method method, Object[] parameter, Builder request) {
		Response response = request.post(getPostEntity(method, parameter));
		validateResponse(configurer, response, "POST");
		return getResponseReturn(response, method);
	}

	private Object sendPutRequest(RequestConfigurer configurer, Method method, Object[] parameter, Builder request) {
		Response response = request.put(getEntity(method, parameter));
		validateResponse(configurer, response, "PUT");
		return getResponseReturn(response, method);
	}

	private Object sendDeleteRequest(RequestConfigurer configurer, Method method, Builder request) {
		Response response = request.delete();
		validateResponse(configurer, response, "DELETE");
		return getResponseReturn(response, method);
	}

	private Object sendHeadRequest(RequestConfigurer configurer, Method method, Builder request) {
		Response response = request.head();
		validateResponse(configurer, response, "HEAD");
		return getResponseReturn(response, method);
	}

	private Object sendOptionsRequest(RequestConfigurer configurer, Method method, Builder request) {
		Response response = request.options();
		validateResponse(configurer, response, "OPTIONS");
		return getResponseReturn(response, method);
	}

	private void validateResponse(RequestConfigurer configurer, Response response, String method) {
		Family family = response.getStatusInfo().getFamily();
		if (family == SERVER_ERROR || family == CLIENT_ERROR) {
			RequestError requestError = new RequestError(configurer, response, method);
			throw new RequestException(requestError);
		}
	}

	private void checkHasNoFormParameter(Method method) {
		if (hasFormParameter(method) || hasMultiPartFormParameter(method)) {
			throw new IllegalStateException("@GET methods can not have @FormParam or @FormDataParam parameters.");
		}
	}

	private Entity<?> getPostEntity(Method method, Object[] parameter) {
		Entity<?> result;
		try {
			result = getEntity(method, parameter);
		} catch (IllegalStateException noEntityException) {
			result = null;
		}
		return result;
	}

	private Entity<?> getEntity(Method method, Object[] parameter) {
		Entity<?> result = null;
		if (hasFormParameter(method)) {
			Form form = computeForm(method, parameter);
			result = Entity.form(form);
		} else if (hasMultiPartFormParameter(method)) {
			MultiPart mp = computeMultiPart(method, parameter);
			result = Entity.entity(mp, mp.getMediaType());
		} else {
			result = determineBodyParameter(method, parameter);
		}
		return result;
	}

	private boolean hasFormParameter(Method method) {
		return ClientHelper.hasFormAnnotation(method, FormParam.class);
	}

	private Form computeForm(Method method, Object[] parameter) {
		Form result = new Form();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			FormParam formParam = extractAnnotation(annotations, FormParam.class);
			if (formParam != null) {
				result.param(formParam.value(), parameter[i].toString());
			}
		}
		return result.asMap().isEmpty() ? null : result;
	}

	private boolean hasMultiPartFormParameter(Method method) {
		return ClientHelper.hasFormAnnotation(method, FormDataParam.class);
	}

	private MultiPart computeMultiPart(Method method, Object[] parameter) {
		FormDataMultiPart result = new FormDataMultiPart();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			FormDataParam param = extractAnnotation(annotations, FormDataParam.class);
			if (param != null) {
				result.field(param.value(), parameter[i], determinePartContentType(parameter[i]));
			}
		}
		return result.getFields().isEmpty() ? null : result;
	}

	private MediaType determinePartContentType(Object parameter) {
		// TODO: how to get the media type? we could use something like @FormData
		if (parameter instanceof BodyPart) {
			return ((BodyPart) parameter).getMediaType();
		}
		if (parameter instanceof ContentDisposition) {
			return MediaType.valueOf(((ContentDisposition) parameter).getType());
		}
		return MediaType.TEXT_PLAIN_TYPE;
	}

	private <T extends Annotation> T extractAnnotation(Annotation[] annotations, Class<T> type) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == type) {
				return type.cast(annotation);
			}
		}
		return null;
	}

	private Entity<?> determineBodyParameter(Method method, Object[] parameter) {
		Entity<?> result = null;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		checkParametersForAnnotation(method, parameterAnnotations);
		int firstNonAnnotatedParameter = getFirstNonAnnotatedParameter(parameterAnnotations);
		if (firstNonAnnotatedParameter != -1) {
			result = Entity.entity(parameter[firstNonAnnotatedParameter], determineContentType(method));
		} else {
			throw new IllegalStateException(
					"Can not find entity for method " + method.getName() + ". It has no non-annotated parameter");
		}
		return result;
	}

	private int getFirstNonAnnotatedParameter(Annotation[][] parameterAnnotations) {
		int firstNonAnnotatedParameter = -1;
		for (int i = 0; i < parameterAnnotations.length; i++) {
			Annotation[] annotations = parameterAnnotations[i];
			if (annotations.length == 0) {
				firstNonAnnotatedParameter = i;
				break;
			} else {
				boolean b = false;
				for (int j = 0; j < annotations.length; j++) {
					if (annotations[i].getClass().equals(PathParam.class)
							|| annotations[i].getClass().equals(FormDataParam.class)
							|| annotations[i].getClass().equals(FormParam.class)
							|| annotations[i].getClass().equals(BeanParam.class)
							|| annotations[i].getClass().equals(CookieParam.class)
							|| annotations[i].getClass().equals(MatrixParam.class)
							|| annotations[i].getClass().equals(HeaderParam.class)
							|| annotations[i].getClass().equals(QueryParam.class)) {
						b = true;
						break;
					}
				}
				if (!b) {
					firstNonAnnotatedParameter = i;
					break;
				}
			}
		}
		return firstNonAnnotatedParameter;
	}

	private void checkParametersForAnnotation(Method method, Annotation[][] parameterAnnotations) {
		if (parameterAnnotations.length == 0) {
			throw new IllegalStateException(
					"Can not find entity for method " + method.getName() + ". It has no paramters.");
		}
	}

	// TODO: Think about a better determination of the content type
	private MediaType determineContentType(Method method) {
		MediaType result = MediaType.TEXT_PLAIN_TYPE;
		if (method.isAnnotationPresent(Consumes.class)) {
			result = MediaTypes.createFrom(method.getAnnotation(Consumes.class)).get(0);
		}
		return result;
	}
}
