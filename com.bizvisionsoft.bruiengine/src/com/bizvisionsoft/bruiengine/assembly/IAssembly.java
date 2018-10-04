package com.bizvisionsoft.bruiengine.assembly;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;

public interface IAssembly {
	
	IBruiContext getContext();

	IBruiService getBruiService();

	Assembly getConfig();

}
