package com.bizvisionsoft.bruiengine.assembly;

import java.util.Locale;

import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.rap.rwt.RWT;

import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.util.Util;

public class GridPartDefaultRender {

	protected Object input;

	protected Assembly config;

	private Locale locale;

	public GridPartDefaultRender() {
		locale = RWT.getLocale();
	}

	public void setInput(Object input) {
		this.input = input;
	}

	public void setGridConfig(Assembly config) {
		this.config = config;
	}

	public void renderCell(ViewerCell cell, Column column, Object value) {
		String format = column.getFormat();
		String text = Util.getFormatText(value, format, locale);
		cell.setText(text);
		// 默认不处理以下的配置
		// cell.setBackground(getBackground(element));
		// cell.setForeground(getForeground(element));
		// cell.setFont(getFont(element));
		// int colSpan = getColumnSpan(element);
		// GridItem gridItem = (GridItem) cell.getViewerRow().getItem();
		// gridItem.setColumnSpan(cell.getColumnIndex(), colSpan);
	}

	public void renderColumnHeader(GridColumn col, Column column) {
		col.setText(column.getText());
	}

	public void renderColumnFooter(GridColumn col, Column column) {
		// col.setFooterText(column.getFootText());
		// TODO 根据配置直接显示
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int compare(Column col, Object e1, Object e2) {
		if (e1 instanceof Comparable<?> && e2 instanceof Comparable<?>) {
			return ((Comparable) e1).compareTo((Comparable) e2);
		}
		return 0;
	}

}
