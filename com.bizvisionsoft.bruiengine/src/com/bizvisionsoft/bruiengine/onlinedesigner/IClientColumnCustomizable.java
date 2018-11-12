package com.bizvisionsoft.bruiengine.onlinedesigner;

import java.util.ArrayList;
import java.util.List;

import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.assembly.IAssembly;
import com.bizvisionsoft.bruiengine.service.UserSession;
import com.bizvisionsoft.service.tools.Check;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public interface IClientColumnCustomizable extends IAssembly {

	public default boolean customize() {
		return Check.isAssignedThen(Customizer.open(getConfig(), getStore()), ret -> {
			customized(ret);
			return true;
		}).orElse(false);
	}

	public void customized(List<Column> result);

	//TODO move to ClientSettingStore.class
	@SuppressWarnings("unchecked")
	public default List<Column> getStore() {
		return (List<Column>) Check
				.isAssignedThen(UserSession.current().getClientSetting("assembly@" + getConfig().getName()),
						s -> new GsonBuilder().create().fromJson(s, new TypeToken<ArrayList<Column>>() {
						}.getType()))
				.orElse(null);
	}

}
