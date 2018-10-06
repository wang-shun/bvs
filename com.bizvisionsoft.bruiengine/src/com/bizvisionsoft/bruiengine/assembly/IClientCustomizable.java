package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.List;

import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.tools.Check;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public interface IClientCustomizable extends IAssembly {

	public default boolean customize() {
		return Check.isAssignedThen(Customizer.open(getConfig(), getStore()), ret -> {
			UserSession.current().saveClientSetting("assembly@" + getConfig().getName(),
					new GsonBuilder().create().toJson(ret));
			customized(ret);
			return true;
		}).orElse(false);
	}

	public void customized(List<Column> result);

	@SuppressWarnings("unchecked")
	public default List<Column> getStore() {
		return (List<Column>) Check
				.isAssignedThen(UserSession.current().getClientSetting("assembly@" + getConfig().getName()),
						s -> new GsonBuilder().create().fromJson(s, new TypeToken<ArrayList<Column>>() {
						}.getType()))
				.orElse(null);
	}

}
