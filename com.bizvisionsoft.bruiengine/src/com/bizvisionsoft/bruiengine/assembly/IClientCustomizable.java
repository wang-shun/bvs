package com.bizvisionsoft.bruiengine.assembly;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;

import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.service.tools.Check;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public interface IClientCustomizable extends IAssembly {

	public default boolean customize() {
		return Check.isAssignedThen(Customizer.open(getConfig(), getStore()), this::customized).orElse(false);
	}

	public boolean customized(List<Column> result);

	public default List<Column> getStore() {
		String attInJson = RWT.getSettingStore().getAttribute("setting_" + getConfig().getName());
		if (Check.isAssigned(attInJson)) {
			try {
				return new GsonBuilder().create().fromJson(attInJson, new TypeToken<ArrayList<Column>>() {
				}.getType());
			} catch (Exception e) {
			}
		}
		return null;
	}

}
