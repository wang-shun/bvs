package com.bizvisionsoft.bruiengine.app.sysman;

import java.util.Optional;

import org.eclipse.jface.dialogs.InputDialog;

import com.bizvisionsoft.annotations.ui.common.Execute;
import com.bizvisionsoft.annotations.ui.common.Inject;
import com.bizvisionsoft.annotations.ui.common.MethodParam;
import com.bizvisionsoft.bruiengine.assembly.GridPart;
import com.bizvisionsoft.bruiengine.service.IBruiContext;
import com.bizvisionsoft.bruiengine.service.IBruiService;
import com.bizvisionsoft.service.SystemService;
import com.bizvisionsoft.service.model.Backup;
import com.bizvisionsoft.serviceconsumer.Services;

public class EditBackupNotesACT {

	@Inject
	private IBruiService brui;

	@Execute
	public void execute(@MethodParam(Execute.CONTEXT) IBruiContext context) {
		context.selected(t -> {
			String notes = Optional.ofNullable(((Backup) t).getNotes()).orElse("");
			InputDialog id = new InputDialog(brui.getCurrentShell(), "备份说明", "编辑备份说明", notes, null)
					.setTextMultiline(true);
			if (id.open() == InputDialog.OK) {
				Services.get(SystemService.class).updateBackupNote(((Backup) t).getId(),id.getValue());
				((Backup) t).setNotes(id.getValue());
				((GridPart)context.getContent()).update(t);
			}
		});
	}

}
