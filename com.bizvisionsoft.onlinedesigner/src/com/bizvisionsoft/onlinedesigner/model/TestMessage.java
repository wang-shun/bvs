package com.bizvisionsoft.onlinedesigner.model;

import com.bizivisionsoft.widgets.util.Layer;
import com.bizvisionsoft.annotations.ui.common.Execute;

public class TestMessage {
	
	@Execute
	public void execute() {
		Layer.message("''<>消息<br>提\n;&%><br></div>示'");
	}

}
