package com.bizvisionsoft.bruidesigner.command;

import com.bizvisionsoft.bruicommons.model.Assembly;

public class CreateGanttAssembly extends AbstractCreateAssembly {


	protected String getType() {
		return Assembly.TYPE_GANTT;
	}

}
