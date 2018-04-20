package com.bizvisionsoft.bruiengine.action;

import com.bizivisionsoft.widgets.diagram.Diagram;
import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public class TreePartZoomOutAction extends Action {

	private Diagram tree;

	@Inject
	private IBruiService bruiService;

	
	public TreePartZoomOutAction(Diagram tree) {
		this.tree = tree;
		setType(Action.TYPE_CUSTOMIZED);
		setImage("/img/zoomout_w.svg");
		setStyle("info");
	}
	
	@Execute
	public void execute(@MethodParam(Execute.PARAM_CONTEXT) IBruiContext context) {
		tree.zoomOut();
	}
	

}
