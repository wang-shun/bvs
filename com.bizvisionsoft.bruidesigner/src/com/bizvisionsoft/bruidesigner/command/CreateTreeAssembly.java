package com.bizvisionsoft.bruidesigner.command;

import com.bizvisionsoft.bruicommons.model.Assembly;

public class CreateTreeAssembly extends AbstractCreateAssembly {


	@Override
	protected String getType() {
		return Assembly.TYPE_TREE;
	}

}
