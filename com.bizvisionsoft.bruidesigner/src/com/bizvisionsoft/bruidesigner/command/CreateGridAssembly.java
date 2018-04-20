package com.bizvisionsoft.bruidesigner.command;

import com.bizvisionsoft.bruicommons.model.Assembly;

public class CreateGridAssembly extends AbstractCreateAssembly {


	@Override
	protected String getType() {
		return Assembly.TYPE_GRID;
	}

}
