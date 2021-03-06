package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruidesigner.view.SiteView;

public class AddPage extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SiteView part = (SiteView) HandlerUtil.getActivePart(event);
		part.addPage();
		return null;
	}

}
