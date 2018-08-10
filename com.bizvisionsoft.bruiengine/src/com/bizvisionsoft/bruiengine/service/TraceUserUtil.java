package com.bizvisionsoft.bruiengine.service;

import java.util.Date;
import java.util.Optional;

import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.Brui;
import com.bizvisionsoft.service.UserService;
import com.bizvisionsoft.service.model.TraceInfo;
import com.bizvisionsoft.service.model.User;
import com.bizvisionsoft.serviceconsumer.Services;

public class TraceUserUtil {

	public static void traceAction(Action action, IBruiContext context) {
		User consigner = Brui.sessionManager.getConsigner();
		User user = Brui.sessionManager.getUser();
		if (Boolean.TRUE.equals(consigner.getTrace())) {
			saveTrace(consigner, action, context, user);
		}
	}
	
	public static void trace(String message) {
		User consigner = Brui.sessionManager.getConsigner();
		User user = Brui.sessionManager.getUser();
		if (Boolean.TRUE.equals(consigner.getTrace())) {
			save(consigner, user,message);
		}		
	}

	private static void save(User consigner, User user,String message) {
		TraceInfo ti = new TraceInfo();
		ti.userId = user.getUserId();
		ti.userName = user.getName();
		ti.consignerId = consigner.getUserId();
		ti.consignerName = consigner.getName();
		ti.actionName = "µÇ³öÏµÍ³";
		ti.date = new Date();
		Services.get(UserService.class).insertTraceInfo(ti);		
	}

	private static void saveTrace(User consigner, Action action, IBruiContext context, User user) {
		TraceInfo ti = new TraceInfo();
		ti.userId = user.getUserId();
		ti.userName = user.getName();
		ti.consignerId = consigner.getUserId();
		ti.consignerName = consigner.getName();
		ti.actionId = action.getId();
		ti.actionName = action.getName();

		Optional.ofNullable(context.getAssembly()).ifPresent(a -> {
			ti.assemblyId = a.getId();
			ti.assemblyName = a.getName();
		});

		Optional.ofNullable(context.getInput()).ifPresent(a -> ti.contextInput = a.toString());
		
		Optional.ofNullable(context.getRootInput()).ifPresent(a -> ti.contextRootInput = a.toString());

		Optional.ofNullable(context.getSelection()).map(s -> s.getFirstElement())
				.ifPresent(em -> ti.contextSelection = em.toString());

		ti.date = new Date();
		
		Services.get(UserService.class).insertTraceInfo(ti);
	}



}
