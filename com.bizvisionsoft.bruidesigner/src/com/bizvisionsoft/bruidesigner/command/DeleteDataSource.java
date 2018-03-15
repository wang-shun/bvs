package com.bizvisionsoft.bruidesigner.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.bizvisionsoft.bruicommons.model.DataSource;
import com.bizvisionsoft.bruidesigner.view.DataSourcesView;

public class DeleteDataSource extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		DataSourcesView part = (DataSourcesView) HandlerUtil.getActivePart(event);
		DataSource assy = (DataSource) ((IStructuredSelection) HandlerUtil.getCurrentSelection(event))
				.getFirstElement();
		part.removeDataSource(assy);
		return null;
	}

}
