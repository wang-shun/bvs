package com.bizvisionsoft.bruiengine.assembly;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.rap.rwt.RWT;

import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.BruiGridRenderEngine;
import com.bizvisionsoft.service.tools.Util;

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
		renderEngine.renderCell(cell, column);
	}

	public String getColumnValue(Object element) {
		// TODO 缺少调用注解时获取值的方法
		Object value = renderEngine.getColumnValue(element, column);
		String text;
		if ((value instanceof Number) && ((Number) value).doubleValue() == 0 && !column.isForceDisplayZero()) {
			text = "";
		} else {
			String format = column.getFormat();
			text = Util.getFormatText(value, format, RWT.getLocale());
		}
		return text;
	}

}
