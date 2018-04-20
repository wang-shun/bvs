package com.bizvisionsoft.bruidesigner.command;

import com.bizvisionsoft.bruicommons.model.Assembly;

public class CreateEditorAssembly extends AbstractCreateAssembly {


	@Override
	protected String getType() {
		return Assembly.TYPE_EDITOR;
	}

}
