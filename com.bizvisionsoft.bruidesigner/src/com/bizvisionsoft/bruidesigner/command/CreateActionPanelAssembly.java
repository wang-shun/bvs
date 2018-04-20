package com.bizvisionsoft.bruidesigner.command;

import com.bizvisionsoft.bruicommons.model.Assembly;

public class CreateActionPanelAssembly extends AbstractCreateAssembly {


	@Override
	protected String getType() {
		return Assembly.TYPE_ACTION_PANEL;
	}

}
