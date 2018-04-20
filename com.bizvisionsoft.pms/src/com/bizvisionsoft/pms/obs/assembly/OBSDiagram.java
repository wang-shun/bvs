package com.bizvisionsoft.pms.obs.assembly;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.bizivisionsoft.widgets.diagram.Diagram;
import com.bizvisionsoft.annotations.ui.common.CreateUI;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.bruiengine.service.BruiAssemblyContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.OBSService;
import com.bizvisionsoft.service.model.IOBSScope;
import com.bizvisionsoft.service.model.OBSItem;
import com.bizvisionsoft.serviceconsumer.Services;

public class OBSDiagram {

	@Inject
	private IBruiService bruiService;

	@Inject
	private BruiAssemblyContext context;

	@CreateUI
	public void createUI(Composite parent) {
		parent.setLayout(new FillLayout());
		Diagram diagram = new Diagram(parent);
		diagram.setContainer(context.getAssembly().getName());
		ObjectId scope_id = ((IOBSScope)context.getRootInput()).getScope_id();
		List<OBSItem> data = Services.get(OBSService.class).getScopeOBS(scope_id);

		diagram.setInputData(data);
	}

}
