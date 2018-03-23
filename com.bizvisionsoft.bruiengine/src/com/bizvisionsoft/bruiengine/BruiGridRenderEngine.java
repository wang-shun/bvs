package com.bizvisionsoft.bruiengine;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.osgi.framework.Bundle;

import com.bizvisionsoft.annotations.AUtil;
import com.bizvisionsoft.annotations.ui.grid.GridRenderColumnFooter;
import com.bizvisionsoft.annotations.ui.grid.GridRenderColumnHeader;
import com.bizvisionsoft.annotations.ui.grid.GridRenderConfig;
import com.bizvisionsoft.annotations.ui.grid.GridRenderInput;
import com.bizvisionsoft.annotations.ui.grid.GridRenderUpdateCell;
import com.bizvisionsoft.bruicommons.model.Assembly;
import com.bizvisionsoft.bruicommons.model.Column;
import com.bizvisionsoft.bruiengine.assembly.GridPartDefaultRender;
import com.bizvisionsoft.bruiengine.service.IServiceWithId;

public class BruiGridRenderEngine extends BruiEngine {

	public static BruiGridRenderEngine create(Assembly grid, IServiceWithId... services) {
		BruiGridRenderEngine eng = (BruiGridRenderEngine) load(grid.getGridRenderBundleId(),
				grid.getGridRenderClassName())// load
						.newInstance();
		eng.setGridConfig(grid);
		return (BruiGridRenderEngine) eng.init(services);
	}

	private static BruiGridRenderEngine load(String bundleId, String className) {
		if (bundleId != null && className != null) {
			Bundle bundle = Platform.getBundle(bundleId);
			try {
				return new BruiGridRenderEngine(bundle.loadClass(className));
			} catch (Exception e) {
				throw new RuntimeException(e.getCause());
			}
		}
		return new BruiGridRenderEngine();
	}

	private GridPartDefaultRender defaultRender;
	private Assembly config;

	BruiGridRenderEngine(Class<?> clazz) {
		super(clazz);
	}

	BruiGridRenderEngine() {
		super();
		defaultRender = new GridPartDefaultRender();
	}

	public void setInput(Object input) {
		if (defaultRender != null)
			defaultRender.setInput(input);
		else
			setFieldValue(GridRenderInput.class, input);
	}

	private BruiGridRenderEngine setGridConfig(Assembly config) {
		this.config = config;
		if (defaultRender != null)
			defaultRender.setGridConfig(config);
		else
			setFieldValue(GridRenderConfig.class, config);
		return this;
	}

	public void renderCell(ViewerCell cell, Column column) {
		Object element = cell.getElement();
		Object value = getColumnValue(element, column);
		if (defaultRender != null) {
			defaultRender.renderCell(cell, column, value);
		} else {
			invokeMethodInjectParams(GridRenderUpdateCell.class, new Object[] { cell, column, value },
					new String[] { GridRenderUpdateCell.PARAM_CELL, GridRenderUpdateCell.PARAM_COLUMN,
							GridRenderUpdateCell.PARAM_VALUE },
					element.toString());
		}
	}

	public void renderHeaderText(GridColumn col, Column c) {
		if (defaultRender != null) {
			defaultRender.renderColumnHeader(col, c);
		} else {
			invokeMethodInjectParams(GridRenderColumnHeader.class, new Object[] { col, c },
					new String[] { GridRenderColumnHeader.PARAM_COLUMN_WIDGET, GridRenderUpdateCell.PARAM_COLUMN },
					null);
		}
	}

	public void renderFoot(GridColumn col, Column c) {
		if (defaultRender != null) {
			defaultRender.renderColumnFooter(col, c);
		} else {
			invokeMethodInjectParams(GridRenderColumnFooter.class, new Object[] { col, c },
					new String[] { GridRenderColumnFooter.PARAM_COLUMN_WIDGET, GridRenderColumnFooter.PARAM_COLUMN },
					null);
		}
	}

	private Object getColumnValue(Object element, Column column) {
		return AUtil.readValue(element, config.getName(), column.getName(), element);
	}

}
