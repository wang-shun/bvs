package com.bizvisionsoft.bruiengine.action;

import java.util.Optional;

import org.bson.Document;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruicommons.model.Action;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.bruiengine.ui.Editor;
import com.bizvisionsoft.service.CommonService;
import com.bizvisionsoft.service.tools.Check;
import com.bizvisionsoft.serviceconsumer.Services;

public class SystemSetting {

	@Inject
	private IBruiService br;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context, @MethodParam(Execute.ACTION) Action action) {
		String name = action.getName();
		String editorId = Check.option(action.getEditorAssemblyId()).orElse(name);
		String parameter = Check.option(action.getSysSettingParameter()).orElse(name);

		Document setting = Optional.ofNullable(Services.get(CommonService.class).getSetting(parameter))
				.orElse(new Document("name", parameter));
		Editor.open(editorId, context, setting, true, (d, r) -> {
			Services.get(CommonService.class).updateSetting(r);
		});
	}

}
