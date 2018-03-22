package com.bizvisionsoft.bruiengine.assembly;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiGridRenderEngine;

public class GridPartColumnLabelProvider extends CellLabelProvider {

	private BruiGridRenderEngine renderEngine;
	private Column column;

	public GridPartColumnLabelProvider(BruiGridRenderEngine renderEngine, Column column) {
		this.renderEngine = renderEngine;
		this.column = column;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update(ViewerCell cell) {
		renderEngine.renderCell(cell,column);
	}


}
