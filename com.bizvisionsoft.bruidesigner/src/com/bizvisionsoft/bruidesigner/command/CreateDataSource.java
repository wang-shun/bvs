package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruidesigner.view.DataSourcesView;

public class CreateDataSource extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DataSourcesView part = (DataSourcesView) HandlerUtil.getActivePart(event);
		part.createDataSource();
		return null;
	}

}
